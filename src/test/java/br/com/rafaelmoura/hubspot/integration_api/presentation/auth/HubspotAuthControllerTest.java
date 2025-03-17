package br.com.rafaelmoura.hubspot.integration_api.presentation.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.auth.HubspotAuthService;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.TokenExchangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HubspotAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HubspotAuthService hubspotAuthService;

    @InjectMocks
    HubspotAuthController hubspotAuthController;

    private final String HUBSPOT_AUTH_BASE_URL = "http://localhost:8080/auth/hubspot";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve gerar a url de redirecionamento para o hubspot com sucesso com sucesso")
    void shouldGenerateAuthorizationUriSuccess() throws Exception {

        String expectedAuthorizationUri = "https://hubtest.com/oauth/authorize?client_id=test-client-id&scope=scope-test&redirect_uri=http://test/auth/hubspot/callback";

        Mockito.when(hubspotAuthService.generateAuthorizationUri()).thenReturn(expectedAuthorizationUri);

        mockMvc.perform(MockMvcRequestBuilders.get(HUBSPOT_AUTH_BASE_URL.concat("/v1/authorization")))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAuthorizationUri))
                .andDo(print());

        Mockito.verify(hubspotAuthService, Mockito.times(1)).generateAuthorizationUri();
    }

    @Test
    @DisplayName(value = "Deve retornar um objeto do tipo TokenExchangeResponseDTO contendo dados do token exchange")
    void shouldReturnTokenExchangeResponseDTOSuccess() throws Exception {

        TokenExchangeResponseDTO tokenExchangeResponseDTO =
                new TokenExchangeResponseDTO("access-token", 10029, "Bearer", "aaaaswwwwwww");

        Mockito.when(hubspotAuthService.tokenExchange(Mockito.anyString())).thenReturn(tokenExchangeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(HUBSPOT_AUTH_BASE_URL.concat("/v1/token-exchange")).param("code", "code-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(tokenExchangeResponseDTO.accessToken()))
                .andExpect(jsonPath("$.token_type").value(tokenExchangeResponseDTO.tokenType()))
                .andExpect(jsonPath("$.refresh_token").value(tokenExchangeResponseDTO.refreshToken()))
                .andExpect(jsonPath("$.expires_in").value(tokenExchangeResponseDTO.expiresIn()))
                .andDo(print());

        Mockito.verify(hubspotAuthService, Mockito.times(1)).tokenExchange(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deve retornar um codigo 400 objeto do tipo ErrorDTO quando parametro code nao for informado")
    void shouldReturnStatus400AndErrorDTOWhenCodeNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(HUBSPOT_AUTH_BASE_URL.concat("/v1/token-exchange")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("MISSING_PARAMETER_EXCEPTION"))
                .andExpect(jsonPath("$.message").value("erro durante a requisição. Parâmetro obrigatório: [code] não informado ou inválido"))
                .andDo(print());

        Mockito.verifyNoInteractions(hubspotAuthService);
    }

    @Test
    @DisplayName(value = "Deve retornar um codigo 400 objeto do tipo ErrorDTO quando parametro code estiver vazio")
    void shouldReturnStatus400AndErrorDTOWhenCodeIsEmpty() throws Exception {

        Mockito.when(hubspotAuthService.tokenExchange("")).thenThrow(new TokenExchangeException("code informado para o token exchange é invalido"));

        mockMvc.perform(MockMvcRequestBuilders.get(HUBSPOT_AUTH_BASE_URL.concat("/v1/token-exchange")).param("code", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("TOKEN_EXCHANGE_ERROR"))
                .andExpect(jsonPath("$.message").value("code informado para o token exchange é invalido"))
                .andDo(print());

        Mockito.verify(hubspotAuthService, Mockito.times(1)).tokenExchange(Mockito.anyString());
    }
}