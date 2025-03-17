package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;

public interface HubspotAuthService {
    String generateAuthorizationUri();
    TokenExchangeResponseDTO tokenExchange(String code);
}
