package br.com.rafaelmoura.hubspot.integration_api.application.service.webhook;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request.CreatedContactWebhookRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.webhook.exceptions.WebhookInvalidSignatureException;
import br.com.rafaelmoura.hubspot.integration_api.presentation.exceptions.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubspotContactWebhookServiceImpl implements HubspotContactWebhookService {

    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.hubspot.client-secret}")
    private String clientSecret;

    @Override
    public void webhookContactCreation(List<CreatedContactWebhookRequestDTO> requestDTO,
                                       String webhookSignature,
                                       String webhookSignatureVersion,
                                       String webhookRequestTimestamp) {

        log.info("[HubspotContactWebhookServiceImpl][webhookContactCreation] - Evento de criação de contato recebido.");

        validateCreatedContactWebHookSignature(requestDTO, webhookSignature, webhookSignatureVersion, webhookRequestTimestamp);

        log.info("[HubspotContactWebhookServiceImpl][webhookContactCreation] - Evento de criação de contato concluido.");
    }

    private void validateCreatedContactWebHookSignature(List<CreatedContactWebhookRequestDTO> requestDTO,
                                                        String webhookSignature,
                                                        String webhookSignatureVersion,
                                                        String webhookRequestTimestamp) {
        log.info("[HubspotContactWebhookServiceImpl][validateCreatedContactWebHookSignature] - iniciando validação da assinatura do webhook de versão: [{}]",
                webhookSignatureVersion);
        try {
            switch (webhookSignatureVersion) {
                case "v1" -> validateV1Signature(requestDTO, webhookSignature);
                case "v2" -> validateV2Signature(requestDTO, webhookSignature);
                case "v3" -> validateV3Signature(requestDTO, webhookSignature, webhookRequestTimestamp);
                default -> {
                    log.error("[HubspotContactWebhookServiceImpl][validateCreatedContactWebHookSignature] - Versão invalida para assinatura do webhook; Versão: [{}]",
                            webhookSignatureVersion);
                    throw new WebhookInvalidSignatureException("Versão invalida para assinatura do webhook");
                }
            }
        } catch (JsonProcessingException ex) {
            log.error("[HubspotContactWebhookServiceImpl][validateCreatedContactWebHookSignature] - Erro durante conversão do payload de requisição");
            throw new SystemException("Erro durante conversão do payload de requisição");
        }

        log.info("[HubspotContactWebhookServiceImpl][validateCreatedContactWebHookSignature] - validação da assinatura do webhook de versão: [{}] concluida.",
                webhookSignatureVersion);
    }

    private void validateV1Signature(List<CreatedContactWebhookRequestDTO> requestDTO, String webhookSignature) throws JsonProcessingException {

        String expectedSignatureHash = clientSecret + objectMapper.writeValueAsString(requestDTO);

        if (!webhookSignature.equals(generateSha256(expectedSignatureHash))) {
            log.error("[HubspotContactWebhookServiceImpl][validateV1Signature] - Assinatura do webhook de versão 1 invalida");
            throw new WebhookInvalidSignatureException("Assinatura do webhook de versão 1 invalida");
        }
    }

    private void validateV2Signature(List<CreatedContactWebhookRequestDTO> requestDTO, String webhookSignature) throws JsonProcessingException {

        String sourceString = clientSecret
                + HttpMethod.POST.name()
                + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
                + objectMapper.writeValueAsString(requestDTO);

        String expectedSignature = generateSha256(new String(sourceString.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));

        if (!webhookSignature.equals(expectedSignature)) {
            log.error("[HubspotContactWebhookServiceImpl][validateV1Signature] - Assinatura do webhook de versão 2 invalida");
            throw new WebhookInvalidSignatureException("Assinatura do webhook de versão 2 invalida");
        }
    }

    private void validateV3Signature(List<CreatedContactWebhookRequestDTO> requestDTO,
                                     String webhookSignature,
                                     String webhookRequestTimestamp) throws JsonProcessingException {

        String sourceString = clientSecret
                + HttpMethod.POST.name()
                + ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
                + objectMapper.writeValueAsString(requestDTO)
                + webhookRequestTimestamp;

        if (!webhookSignature.equals(generateSha256(sourceString))) {
            log.error("[HubspotContactWebhookServiceImpl][validateV1Signature] - Assinatura do webhook de versão 3 invalida");
            throw new WebhookInvalidSignatureException("Assinatura do webhook de versão 3 invalida");
        }
    }

    protected String generateSha256(String source)  {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(source.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
