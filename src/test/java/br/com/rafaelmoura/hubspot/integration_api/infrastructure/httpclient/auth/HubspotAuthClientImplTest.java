package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.configuration.HubspotAuthConfigProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.naming.directory.InvalidAttributesException;

public class HubspotAuthClientImplTest {

    @Mock
    private HubspotAuthConfigProperties hubspotAuthConfigProperties;

    @InjectMocks
    private HubspotAuthClientImpl hubspotAuthClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve gerar a url de autorização com sucesso para o Hubspot")
    void shouldGenerateAuthorizationUriSuccess() {

        String expectedAuthorizationUri = "https://hubtest.com/oauth/authorize?client_id=test-client-id&scopes=scope-test&redirect_uri=http://test/auth/hubspot/callback";

        Mockito.when(hubspotAuthConfigProperties.getClientId()).thenReturn("test-client-id");
        Mockito.when(hubspotAuthConfigProperties.getAuthorizationUri()).thenReturn("https://hubtest.com/oauth/authorize");
        Mockito.when(hubspotAuthConfigProperties.getScope()).thenReturn("scope-test");
        Mockito.when(hubspotAuthConfigProperties.getRedirectUri()).thenReturn("http://test/auth/hubspot/callback");

        String authorizationUri = hubspotAuthClient.generateAuthorizationUri();

        Assertions.assertNotNull(authorizationUri);
        Assertions.assertEquals(expectedAuthorizationUri, authorizationUri);

        Mockito.verify(hubspotAuthConfigProperties, Mockito.times(1)).getClientId();
        Mockito.verify(hubspotAuthConfigProperties, Mockito.times(1)).getAuthorizationUri();
        Mockito.verify(hubspotAuthConfigProperties, Mockito.times(1)).getScope();
        Mockito.verify(hubspotAuthConfigProperties, Mockito.times(1)).getRedirectUri();

    }
}
