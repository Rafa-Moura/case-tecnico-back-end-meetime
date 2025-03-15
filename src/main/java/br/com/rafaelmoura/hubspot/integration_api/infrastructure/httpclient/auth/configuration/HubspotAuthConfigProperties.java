package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class HubspotAuthConfigProperties {

    @Value("${spring.security.oauth2.client.hubspot.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.hubspot.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.hubspot.scope}")
    private String scope;
    @Value("${spring.security.oauth2.client.hubspot.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.hubspot.authorization-grant-type}")
    private String authorizationGrantType;
    @Value("${spring.security.oauth2.client.hubspot.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.client.hubspot.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.hubspot.introspect-token-uri}")
    private String introspectTokenUri;
    @Value("${spring.security.oauth2.client.hubspot.introspect-refresh-token-uri}")
    private String introspectRefreshTokenUri;
}