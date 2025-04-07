package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.IntrospectTokenResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.entities.HubspotAuthUser;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.TokenExchangeException;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.repository.HubspotAuthUserRepository;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.HubspotAuthClient;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo.TokenExchangeResponseVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class HubspotAuthServiceImplTest {

    @Mock
    HubspotAuthClient hubspotAuthClient;

    @Mock
    HubspotAuthUserRepository hubspotAuthUserRepository;

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

    @Test
    @DisplayName(value = "Deve retornar com sucesso um objeto do tipo TokenExchangeResponseDTO ao finalizar o token exchange")
    void shouldReturnTokenExchangeResponseDTOSuccess() {

        TokenExchangeResponseVO exchangeResponseVO =
                new TokenExchangeResponseVO("access_token", 1800, "Bearer", "refresh_token");

        Mockito.when(hubspotAuthClient.tokenExchange(Mockito.anyString())).thenReturn(exchangeResponseVO);

        Mockito.when(hubspotAuthClient.introspectToken(Mockito.anyString()))
                .thenReturn(new IntrospectTokenResponseDTO("rf@gmail.com", 1231444, "aaaaeeeerrrr", List.of("oauth")));

        Mockito.when(hubspotAuthUserRepository.save(Mockito.any())).thenReturn(new HubspotAuthUser());

        TokenExchangeResponseDTO responseDTO = hubspotAuthService.tokenExchange("code");

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("access_token", responseDTO.accessToken());
        Assertions.assertEquals(1800, responseDTO.expiresIn());
        Assertions.assertEquals("Bearer", responseDTO.tokenType());
        Assertions.assertEquals("rf@gmail.com", responseDTO.user());

        Mockito.verify(hubspotAuthClient, Mockito.times(1)).tokenExchange(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deve lançar uma TokenExchangeException quando o code estiver vazio")
    void shouldThrownTokenExchangeExceptionWhenCodeIsEmpty() {

        Throwable error = Assertions.assertThrows(TokenExchangeException.class,
                () -> hubspotAuthService.tokenExchange(""));

        Assertions.assertNotNull(error);
        Assertions.assertEquals(TokenExchangeException.class, error.getClass());

        Mockito.verifyNoInteractions(hubspotAuthClient);
    }
}