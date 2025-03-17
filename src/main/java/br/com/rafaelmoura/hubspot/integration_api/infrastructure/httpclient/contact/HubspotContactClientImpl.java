package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.*;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubspotContactClientImpl implements HubspotContactClient {

    @Value("${clients.hubspot.contact.uri}")
    private String hubspotContactUri;

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Override
    public HubspotContactResponseVO createContact(HubspotContactRequestDTO hubspotContactRequestDTO, String accessToken) {

        log.info("[HubspotContactClientImpl][createContact] - Iniciando requisição no Hubspot para criação de contato. Email: [{}]", hubspotContactRequestDTO.properties().email());

        try {
            HttpResponse<String> createContactResponse = httpClient
                    .send(createContactRequest(hubspotContactRequestDTO, accessToken), HttpResponse.BodyHandlers.ofString());

            validateCreateContactHubspotResponse(createContactResponse);

            log.info("[HubspotContactClientImpl][createContact] - requisição no Hubspot para criação de contato concluída. Email: [{}]", hubspotContactRequestDTO.properties().email());
            return objectMapper.readValue(createContactResponse.body(), HubspotContactResponseVO.class);
        } catch (IOException e) {
            log.error("[HubspotContactClientImpl][createContact] - IOException durante criação de contato na Hubspot. Cause: [{}]", e.getMessage());
            throw new HubspotContactSystemException(e.getMessage());
        } catch (InterruptedException e) {
            log.error("[HubspotContactClientImpl][createContact] - InterruptedException durante criação de contato na Hubspot. Cause: [{}]", e.getMessage());
            Thread.currentThread().interrupt();
            throw new HubspotContactSystemException(e.getMessage());
        }
    }

    private static void validateCreateContactHubspotResponse(HttpResponse<String> createContactResponse) {

        int responseStatusCode = createContactResponse.statusCode();

        if (HttpStatus.UNAUTHORIZED.value() == responseStatusCode) {
            log.error("[HubspotContactClientImpl][createContact] - criação de contato no hubspot não autorizada. Status: [{}] body: [{}]",
                    responseStatusCode, createContactResponse.body());

            throw new HubspotContactUnauthorizedException("Criação de contato não autorizada no Hubspot");
        }

        if (HttpStatus.BAD_REQUEST.value() == responseStatusCode) {
            log.error("[HubspotContactClientImpl][createContact] - erro durante criação de contato no hubspot. Status: [{}] body: [{}]",
                    responseStatusCode, createContactResponse.body());

            throw new HubspotContactBusinessException("Erro durante criação de contato no hubspot");
        }

        if (HttpStatus.CONFLICT.value() == responseStatusCode) {
            log.error("[HubspotContactClientImpl][createContact] - erro na criação de contato no hubspot. O contato informado já existe. Status: [{}] body: [{}]",
                    responseStatusCode, createContactResponse.body());

            throw new HubspotContactAlreadyExistsException("erro na criação de contato no hubspot. O contato informado já existe");
        }

        if (HttpStatus.TOO_MANY_REQUESTS.value() == responseStatusCode) {
            log.error("[HubspotContactClientImpl][createContact] - erro na criação de contato no hubspot. Limite de requisições diárias atingido. Status: [{}] body: [{}]",
                    responseStatusCode, createContactResponse.body());

            throw new HubspotContactRateLimitException("erro na criação de contato no hubspot. Limite de requisições diárias atingido");
        }
    }

    private HttpRequest createContactRequest(HubspotContactRequestDTO hubspotContactRequestDTO, String accessToken) {

        return HttpRequest.newBuilder()
                .uri(URI.create(hubspotContactUri))
                .POST(HttpRequest.BodyPublishers.ofString(createContactBody(hubspotContactRequestDTO)))
                .headers(
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE,
                        "Authorization", accessToken
                )
                .build();
    }

    private String createContactBody(HubspotContactRequestDTO hubspotContactRequestDTO) {

        try {
            return objectMapper.writeValueAsString(hubspotContactRequestDTO);
        } catch (JsonProcessingException e) {
            log.error("[HubspotContactClientImpl][createContactBody] - erro ao converter o objeto em json para requisição de criação de contato");
            throw new HubspotContactSystemException(e.getMessage());
        }
    }
}