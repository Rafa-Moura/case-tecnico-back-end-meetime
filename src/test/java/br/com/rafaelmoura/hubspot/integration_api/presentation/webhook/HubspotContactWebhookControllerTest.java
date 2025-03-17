package br.com.rafaelmoura.hubspot.integration_api.presentation.webhook;

import br.com.rafaelmoura.hubspot.integration_api.application.service.webhook.HubspotContactWebhookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HubspotContactWebhookControllerTest {

    @MockitoBean
    HubspotContactWebhookService hubspotContactWebhookService;

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    HubspotContactWebhookController hubspotContactWebhookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve processar com sucesso um evento de criação de contato do webhook da hubspot")
    void shouldProcessContactCreationSuccess() throws Exception {

        String json = "[{\"objectId\":1246978,\"changeSource\":\"IMPORT\",\"eventId\":3816279480,\"subscriptionId\":22,\"portalId\":33,\"appId\":1160452,\"occurredAt\":1462216307945,\"eventType\":\"contact.creation\",\"attemptNumber\":0}]";

        Mockito.doNothing().when(hubspotContactWebhookService)
                .webhookContactCreation(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/webhook/contacts/v1")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-HubSpot-Signature", "AAAAAA")
                        .header("X-HubSpot-Signature-Version", "AAAAAA")
                        .header("X-HubSpot-Request-Timestamp", "2025-03-16T20:12:12")
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(hubspotContactWebhookService, Mockito.times(1))
                .webhookContactCreation(Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deve retornar status 401 quando o header X-Hubspot-Signature não for informado")
    void shouldReturnUnauthorizedWhenSignatureHeaderIsEmpty() throws Exception {

        String json = "[{\"objectId\":1246978,\"changeSource\":\"IMPORT\",\"eventId\":3816279480,\"subscriptionId\":22,\"portalId\":33,\"appId\":1160452,\"occurredAt\":1462216307945,\"eventType\":\"contact.creation\",\"attemptNumber\":0}]";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/webhook/contacts/v1")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-HubSpot-Signature-Version", "AAAAAA")
                        .header("X-HubSpot-Request-Timestamp", "2025-03-16T20:12:12")
                        .content(json)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());

        Mockito.verifyNoInteractions(hubspotContactWebhookService);
    }

    @Test
    @DisplayName(value = "Deve retornar status 400 quando um header obrigatório não for informado")
    void shouldReturnBadRequestWhenRequiredHeaderIsEmpty() throws Exception {

        String json = "[{\"objectId\":1246978,\"changeSource\":\"IMPORT\",\"eventId\":3816279480,\"subscriptionId\":22,\"portalId\":33,\"appId\":1160452,\"occurredAt\":1462216307945,\"eventType\":\"contact.creation\",\"attemptNumber\":0}]";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/webhook/contacts/v1")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-HubSpot-Signature", "AAAAAA")
                        .header("X-HubSpot-Request-Timestamp", "2025-03-16T20:12:12")
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
