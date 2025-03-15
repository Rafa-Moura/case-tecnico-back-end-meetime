package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

public interface HubspotAuthClient {
    String generateAuthorizationUri();
}
