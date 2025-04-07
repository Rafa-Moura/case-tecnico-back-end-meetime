package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.IntrospectTokenResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo.TokenExchangeResponseVO;

public interface HubspotAuthClient {
    String generateAuthorizationUri();
    TokenExchangeResponseVO tokenExchange(String code);
    IntrospectTokenResponseDTO introspectToken(String accessToken);
}
