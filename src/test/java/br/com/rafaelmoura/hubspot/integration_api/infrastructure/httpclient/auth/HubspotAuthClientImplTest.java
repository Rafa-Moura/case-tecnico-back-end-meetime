package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.HubspotAuthBusinessException;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.HubspotAuthSystemException;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.configuration.HubspotAuthConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class HubspotAuthClientImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<Object> httpResponse;

    @InjectMocks
    private HubspotAuthClientImpl hubspotAuthClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HubspotAuthConfigProperties hubspotAuthConfigProperties = new HubspotAuthConfigProperties(
                "test-client-id",
                "test-client-secret",
                "scope-test",
                "http://test/auth/hubspot/callback",
                "authorization_code",
                "https://hubtest.com/oauth/authorize",
                "https://hubtest.com/oauth/token",
                "https://hubtest.com/oauth/access-tokens",
                "https://hubtest.com/oauth/refresh-tokens"
        );

        hubspotAuthClient = new HubspotAuthClientImpl(hubspotAuthConfigProperties, httpClient, new ObjectMapper());
    }

    @Test
    @DisplayName(value = "Deve gerar a url de autorização com sucesso para o Hubspot")
    void shouldGenerateAuthorizationUriSuccess() {

        String expectedAuthorizationUri = "https://hubtest.com/oauth/authorize?client_id=test-client-id&scopes=scope-test&redirect_uri=http://test/auth/hubspot/callback";

        String authorizationUri = hubspotAuthClient.generateAuthorizationUri();

        Assertions.assertNotNull(authorizationUri);
        Assertions.assertEquals(expectedAuthorizationUri, authorizationUri);
    }

    @Test
    @DisplayName(value = "Deve realizar a troca do code pelo access_token sucess")
    void shouldTokenExchangeSuccess() throws IOException, InterruptedException {

        String validTokenExchangeBody = "{" +
                "    \"access_token\": \"access_token\"," +
                "    \"expires_in\": 1800," +
                "    \"token_type\": \"bearer\"," +
                "    \"refresh_token\": \"refresh_token\"" +
                "}";

        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(validTokenExchangeBody);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);

        TokenExchangeResponseDTO tokenExchangeResponseDTO = hubspotAuthClient.tokenExchange("code-test");

        Assertions.assertNotNull(tokenExchangeResponseDTO);
        Assertions.assertEquals("access_token", tokenExchangeResponseDTO.accessToken());
        Assertions.assertEquals(1800, tokenExchangeResponseDTO.expiresIn());
        Assertions.assertEquals("bearer", tokenExchangeResponseDTO.tokenType());
        Assertions.assertEquals("refresh_token", tokenExchangeResponseDTO.refreshToken());

        Mockito.verify(httpResponse, Mockito.times(2)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar uma HubspotAuthBusinessException quando a troca do code pelo access_token retornar um HTTP4XX")
    void shouldThrownHubspotAuthBusinessExceptionWhenStatus4xx() throws IOException, InterruptedException {

        Mockito.when(httpResponse.statusCode()).thenReturn(404);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);

        Throwable error = Assertions.assertThrows(HubspotAuthBusinessException.class,
                () -> hubspotAuthClient.tokenExchange("code-test"));

        Assertions.assertNotNull(error);
        Assertions.assertEquals(HubspotAuthBusinessException.class, error.getClass());

        Mockito.verify(httpResponse, Mockito.times(2)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar uma HubspotAuthSystemException quando a troca do code pelo access_token retornar um HTTP5XX")
    void shouldThrownHubspotAuthSystemExceptionWhenStatus5xx() throws IOException, InterruptedException {

        Mockito.when(httpResponse.statusCode()).thenReturn(504);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);

        Throwable error = Assertions.assertThrows(HubspotAuthSystemException.class,
                () -> hubspotAuthClient.tokenExchange("code-test"));

        Assertions.assertNotNull(error);
        Assertions.assertEquals(HubspotAuthSystemException.class, error.getClass());

        Mockito.verify(httpResponse, Mockito.times(3)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar uma HubspotAuthSystemException quando a troca do code pelo access_token gerar um IOException")
    void shouldThrownHubspotAuthSystemExceptionWhenIOExceptionOccurred() throws IOException, InterruptedException {

        String invalidTokenExchangeBody = "{";

        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(invalidTokenExchangeBody);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);

        Throwable error = Assertions.assertThrows(HubspotAuthSystemException.class,
                () -> hubspotAuthClient.tokenExchange("code-test"));

        Assertions.assertNotNull(error);
        Assertions.assertEquals(HubspotAuthSystemException.class, error.getClass());

        Mockito.verify(httpResponse, Mockito.times(2)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar uma HubspotAuthSystemException quando a troca do code pelo access_token gerar um InterruptedException")
    void shouldThrownHubspotAuthSystemExceptionWhenInterruptedExceptionOccurred() throws IOException, InterruptedException {

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenThrow(new InterruptedException("error"));

        Throwable error = Assertions.assertThrows(HubspotAuthSystemException.class,
                () -> hubspotAuthClient.tokenExchange("code-test"));

        Assertions.assertNotNull(error);
        Assertions.assertEquals(HubspotAuthSystemException.class, error.getClass());

        Mockito.verifyNoInteractions(httpResponse);
    }
}