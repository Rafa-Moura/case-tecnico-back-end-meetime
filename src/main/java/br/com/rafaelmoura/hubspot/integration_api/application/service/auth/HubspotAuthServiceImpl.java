package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

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
        return hubspotAuthClient.generateAuthorizationUri();
    }
}