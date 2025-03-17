package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;

public interface HubspotAuthClient {
    String generateAuthorizationUri();
    TokenExchangeResponseDTO tokenExchange(String code);
}
