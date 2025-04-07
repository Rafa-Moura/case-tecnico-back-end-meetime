package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.IntrospectTokenResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.HubspotAuthBusinessException;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.HubspotAuthSystemException;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.configuration.HubspotAuthConfigProperties;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo.TokenExchangeResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubspotAuthClientImpl implements HubspotAuthClient {

    private final HubspotAuthConfigProperties hubspotAuthConfigProperties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Override
    public String generateAuthorizationUri() {

        log.info("[HubspotAuthClientImpl][generateAuthorizationUri] - gerando url de autorização do Hubspot");

        String authorizationUri = String.format("%s?client_id=%s&scopes=%s&redirect_uri=%s",
                hubspotAuthConfigProperties.getAuthorizationUri(),
                hubspotAuthConfigProperties.getClientId(),
                hubspotAuthConfigProperties.getScope().replace(",", "%20"),
                hubspotAuthConfigProperties.getRedirectUri());

        log.info("[HubspotAuthClientImpl][generateAuthorizationUri] - url de autorização do Hubspot gerada com sucesso");
        return authorizationUri;
    }

    @Override
    public TokenExchangeResponseVO tokenExchange(String code) {
        log.info("[HubspotAuthClientImpl][tokenExchange] - iniciando fluxo de token exchange na Hubspot. Code: [{}]", code);

        try {
            HttpResponse<String> tokenExchangeResponse = httpClient.send(tokenExchangeRequest(code), HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(tokenExchangeResponse.statusCode()).is4xxClientError()) {
                log.error("[HubspotAuthClientImpl][tokenExchange] - business erro durante fluxo de token exchange. Status: [{}] body: [{}]", tokenExchangeResponse.statusCode(), tokenExchangeResponse.body());

                throw new HubspotAuthBusinessException("business erro durante fluxo de token exchange");
            }

            if (HttpStatus.valueOf(tokenExchangeResponse.statusCode()).is5xxServerError()) {
                log.error("[HubspotAuthClientImpl][tokenExchange] - server erro durante fluxo de token exchange. Status: [{}] body: [{}]", tokenExchangeResponse.statusCode(), tokenExchangeResponse.body());

                throw new HubspotAuthSystemException("server erro durante fluxo de token exchange");
            }

            log.info("[HubspotAuthClientImpl][tokenExchange] - fluxo de token exchange na Hubspot concluído com sucesso. Code: [{}]", code);
            return objectMapper.readValue(tokenExchangeResponse.body(), TokenExchangeResponseVO.class);
        } catch (IOException e) {
            log.error("[HubspotAuthClientImpl][tokenExchange] - IOException durante token exchange. Causa: [{}]", e.getMessage());
            throw new HubspotAuthSystemException(e.getMessage());
        } catch (InterruptedException e) {
            log.error("[HubspotAuthClientImpl][tokenExchange] - InterruptedException durante token exchange. Causa: [{}]", e.getMessage());
            Thread.currentThread().interrupt();
            throw new HubspotAuthSystemException(e.getMessage());
        }
    }

    @Override
    public IntrospectTokenResponseDTO introspectToken(String accessToken) {

        log.info("[HubspotAuthClientImpl][introspectToken] - Iniciando Introspect Token no Hubspot");

        HttpRequest introspectTokenRequest = introspectTokenRequest(accessToken);

        try {
            HttpResponse<String> introspectTokenResponse = httpClient.send(introspectTokenRequest, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(introspectTokenResponse.statusCode()).is4xxClientError()) {
                log.error("[HubspotAuthClientImpl][introspectToken] - business erro durante fluxo de Introspect Token. Status: [{}] body: [{}]",
                        introspectTokenResponse.statusCode(), introspectTokenResponse.body());

                throw new HubspotAuthBusinessException("business erro durante fluxo de Introspect Token");
            }

            if (HttpStatus.valueOf(introspectTokenResponse.statusCode()).is5xxServerError()) {
                log.error("[HubspotAuthClientImpl][introspectToken] - server erro durante fluxo de Introspect Token. Status: [{}] body: [{}]",
                        introspectTokenResponse.statusCode(), introspectTokenResponse.body());

                throw new HubspotAuthSystemException("server erro durante fluxo de Introspect Token");
            }

            log.info("[HubspotAuthClientImpl][introspectToken] - Finalizando Introspect Token no Hubspot");
            return objectMapper.readValue(introspectTokenResponse.body(), IntrospectTokenResponseDTO.class);
        } catch (IOException e) {
            log.error("[HubspotAuthClientImpl][introspectToken] - IOException durante Introspect Token. Causa: [{}]", e.getMessage());
            throw new HubspotAuthSystemException(e.getMessage());
        } catch (InterruptedException e) {
            log.error("[HubspotAuthClientImpl][introspectToken] - InterruptedException durante Introspect Token. Causa: [{}]", e.getMessage());
            Thread.currentThread().interrupt();
            throw new HubspotAuthSystemException(e.getMessage());
        }
    }

    private HttpRequest introspectTokenRequest(String accessToken) {

        return HttpRequest.newBuilder()
                .uri(URI.create(hubspotAuthConfigProperties.getIntrospectTokenUri().concat("/").concat(accessToken)))
                .GET()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpRequest tokenExchangeRequest(String code) {

        return HttpRequest.newBuilder()
                .uri(URI.create(hubspotAuthConfigProperties.getTokenUri()))
                .POST(HttpRequest.BodyPublishers.ofString(tokenExchangeBody(code)))
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    private String tokenExchangeBody(String code) {
        Map<String, Object> body = Map.of("client_id", hubspotAuthConfigProperties.getClientId(),
                "client_secret", hubspotAuthConfigProperties.getClientSecret(),
                "grant_type", hubspotAuthConfigProperties.getAuthorizationGrantType(),
                "redirect_uri", hubspotAuthConfigProperties.getRedirectUri(),
                "code", code);

        return body.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

}