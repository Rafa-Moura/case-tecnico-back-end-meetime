package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.TokenExchangeException;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.HubspotAuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubspotAuthServiceImpl implements HubspotAuthService{

    private final HubspotAuthClient hubspotAuthClient;

    @Override
    public String generateAuthorizationUri() {
        log.info("[HubspotAuthServiceImpl][generateAuthorizationUri] - iniciando fluxo para gerar uri de autorização no HubspotAuthClient");

        String authorizationUri = hubspotAuthClient.generateAuthorizationUri();

        log.info("[HubspotAuthServiceImpl][generateAuthorizationUri] - fluxo para gerar uri de autorização no HubspotAuthClient finalizado");
        return authorizationUri;
    }

    @Override
    public TokenExchangeResponseDTO tokenExchange(String code) {
        log.info("[HubspotAuthServiceImpl][tokenExchange] - iniciando fluxo de token exchange no HubspotAuthClient. Code: [{}]", code);

        if (code.isBlank()) {
            log.error("[HubspotAuthServiceImpl][tokenExchange] - code informado para o token exchange é invalido");
            throw new TokenExchangeException("code informado para o token exchange é invalido");
        }

        TokenExchangeResponseDTO tokenExchangeResponseDTO = hubspotAuthClient.tokenExchange(code);

        log.info("[HubspotAuthServiceImpl][tokenExchange] - fluxo de token exchange no HubspotAuthClient finalizado. Code: [{}]", code);
        return tokenExchangeResponseDTO;
    }
}