package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.configuration.HubspotAuthConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HubspotAuthClientImpl implements HubspotAuthClient{

    private final HubspotAuthConfigProperties hubspotAuthConfigProperties;

    @Override
    public String generateAuthorizationUri() {

        log.info("[HubspotAuthClientImpl][generateAuthorizationUri] - gerando url de autorização do Hubspot");

        String authorizationUri = String.format("%s?client_id=%s&scopes=%s&redirect_uri=%s",
                hubspotAuthConfigProperties.getAuthorizationUri(),
                hubspotAuthConfigProperties.getClientId(),
                hubspotAuthConfigProperties.getScope(),
                hubspotAuthConfigProperties.getRedirectUri());

        log.info("[HubspotAuthClientImpl][generateAuthorizationUri] - url de autorização do Hubspot gerada com sucesso");
        return authorizationUri;
    }
}