package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.HubspotAuthClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HubspotAuthServiceImplTest {

    @Mock
    HubspotAuthClient hubspotAuthClient;

    @InjectMocks
    HubspotAuthServiceImpl hubspotAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve retornar com sucesso a uri de autorização do Hubspot")
    void shouldReturnAuthorizationUriSuccess() {

        String expectedAuthorizationUri = "https://hubtest.com/oauth/authorize?client_id=test-client-id&scope=scope-test&redirect_uri=http://test/auth/hubspot/callback";

        Mockito.when(hubspotAuthClient.generateAuthorizationUri())
                .thenReturn(expectedAuthorizationUri);

        String actualAuthorizationUri = hubspotAuthService.generateAuthorizationUri();

        Assertions.assertNotNull(actualAuthorizationUri);
        Assertions.assertEquals(expectedAuthorizationUri, actualAuthorizationUri);
        Mockito.verify(hubspotAuthClient, Mockito.times(1)).generateAuthorizationUri();
    }
}
