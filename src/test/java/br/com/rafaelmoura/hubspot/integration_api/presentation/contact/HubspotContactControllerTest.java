package br.com.rafaelmoura.hubspot.integration_api.presentation.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.ContactPropertiesResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.contact.HubspotContactService;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.HubspotContactAlreadyExistsException;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.HubspotContactBusinessException;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.HubspotContactRateLimitException;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.HubspotContactSystemException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HubspotContactControllerTest {

    public static final String CONTACTS_ENDPOINT_BASE_URL = "http://localhost:8080/api/contacts/v1";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HubspotContactService hubspotContactService;

    @InjectMocks
    HubspotContactController hubspotContactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve criar com sucesso um novo contato no Hubspot")
    void shouldCreateContactSuccess() throws Exception {

        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        HubspotContactResponseDTO hubspotContactResponseDTO = new HubspotContactResponseDTO(
                1L,
                "1",
                new ContactPropertiesResponseDTO("rf@gmail.com", "rafael", "moura")
        );

        Mockito.when(hubspotContactService.createContact(Mockito.any(), Mockito.anyString()))
                .thenReturn(hubspotContactResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Authorization", "Bearer access_token")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contactId").value(1))
                .andExpect(jsonPath("$.properties.firstname").value("rafael"))
                .andExpect(jsonPath("$.properties.lastname").value("moura"))
                .andExpect(jsonPath("$.properties.email").value("rf@gmail.com"))
                .andDo(print());

        Mockito.verify(hubspotContactService, Mockito.times(1))
                .createContact(Mockito.any(), Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deve retornar status 401 quando header Authorization estiver ausente")
    void shouldReturnStatusUnauthorizedWhenAuthorizationHeaderIsNull() throws Exception {

        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("MISSING_HEADER_EXCEPTION"))
                .andExpect(jsonPath("$.message").value("Header de autorização invalido ou não informado"))
                .andDo(print());

        Mockito.verifyNoInteractions(hubspotContactService);
    }

    @Test
    @DisplayName(value = "Deve retornar status 400 quando algum valor no payload de requisição estiver invalido")
    void shouldReturnStatusBadRequestWhenAnyRequestBodyFieldIsInvalid() throws Exception {

        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rfl.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].title").value("properties.email"))
                .andExpect(jsonPath("$.[0].message").value("favor preencher com um email valido"))
                .andDo(print());

        Mockito.verifyNoInteractions(hubspotContactService);
    }

    @Test
    @DisplayName(value = "deve retornar status 409 conflict quando ocorrer um HubspotContactAlreadyExistsException")
    void shouldReturnConflictWhenHubspotContactAlreadyExistsThrown() throws Exception {

        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        Mockito.when(hubspotContactService.createContact(Mockito.any(), Mockito.anyString()))
                .thenThrow(new HubspotContactAlreadyExistsException("erro na criação de contato no hubspot. O contato informado já existe"));

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Authorization", "Bearer access_token")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("CONTACT_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("erro na criação de contato no hubspot. O contato informado já existe"))
                .andDo(print());

        Mockito.verify(hubspotContactService, Mockito.times(1))
                .createContact(Mockito.any(), Mockito.anyString());

    }

    @Test
    @DisplayName(value = "deve retornar um status 400 quando ocorrer um HubspotContactBusinessException")
    void shouldReturnBadRequestWhenHubspotContactBusinessExceptionThrown() throws Exception {
        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        Mockito.when(hubspotContactService.createContact(Mockito.any(), Mockito.anyString()))
                .thenThrow(new HubspotContactBusinessException("Erro durante criação de contato no hubspot"));

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Authorization", "Bearer access_token")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("CONTACT_BUSINESS_EXCEPTION"))
                .andExpect(jsonPath("$.message").value("Erro durante criação de contato no hubspot"))
                .andDo(print());

        Mockito.verify(hubspotContactService, Mockito.times(1))
                .createContact(Mockito.any(), Mockito.anyString());
    }

    @Test
    @DisplayName(value = "deve retornar um status 500 quando ocorrer um HubspotContactSystemException")
    void shouldReturnBadRequestWhenHubspotContactSystemExceptionThrown() throws Exception {
        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        Mockito.when(hubspotContactService.createContact(Mockito.any(), Mockito.anyString()))
                .thenThrow(new HubspotContactSystemException("IOException"));

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Authorization", "Bearer access_token")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("CONTACT_SYSTEM_EXCEPTION"))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno. Contate um administrador do sistema para maiores informações"))
                .andDo(print());

        Mockito.verify(hubspotContactService, Mockito.times(1))
                .createContact(Mockito.any(), Mockito.anyString());
    }

    @Test
    @DisplayName(value = "deve retornar um status 429 quando o maximo de requisições para o hubspot for atingido")
    void shouldReturnBadRequestWhenHubspotContactRateLimitExceptionThrown() throws Exception {
        String requestBody = "{" +
                "  \"properties\": {" +
                "    \"email\": \"rf@gmail.com\"," +
                "    \"lastname\": \"moura\"," +
                "    \"firstname\": \"rafael\"" +
                "  }" +
                "}";

        Mockito.when(hubspotContactService.createContact(Mockito.any(), Mockito.anyString()))
                .thenThrow(new HubspotContactRateLimitException("erro na criação de contato no hubspot. Limite de requisições diárias atingido"));

        mockMvc.perform(MockMvcRequestBuilders.post(CONTACTS_ENDPOINT_BASE_URL)
                        .header("Authorization", "Bearer access_token")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                )
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.title").value("CONTACT_RATE_LIMIT_EXCEPTION"))
                .andExpect(jsonPath("$.message").value("erro na criação de contato no hubspot. Limite de requisições diárias atingido"))
                .andDo(print());

        Mockito.verify(hubspotContactService, Mockito.times(1))
                .createContact(Mockito.any(), Mockito.anyString());
    }
}