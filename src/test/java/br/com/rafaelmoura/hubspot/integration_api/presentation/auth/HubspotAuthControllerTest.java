package br.com.rafaelmoura.hubspot.integration_api.presentation.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.service.auth.HubspotAuthService;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.configuration.HubspotAuthConfigProperties;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HubspotAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HubspotAuthService hubspotAuthService;

    @MockitoBean
    HubspotAuthConfigProperties hubspotAuthConfigProperties;

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
}