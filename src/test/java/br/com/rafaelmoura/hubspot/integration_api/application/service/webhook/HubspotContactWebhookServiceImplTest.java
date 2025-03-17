package br.com.rafaelmoura.hubspot.integration_api.application.service.webhook;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request.CreatedContactWebhookRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.webhook.exceptions.WebhookInvalidSignatureException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class HubspotContactWebhookServiceImplTest {

    private String clientSecret;

    @InjectMocks
    private HubspotContactWebhookServiceImpl hubspotContactWebhookService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientSecret = "1234";
        objectMapper = new ObjectMapper();
        hubspotContactWebhookService = new HubspotContactWebhookServiceImpl(objectMapper);
        ReflectionTestUtils.setField(hubspotContactWebhookService, "clientSecret", clientSecret);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        request.setMethod("POST");

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    @DisplayName(value = "Deve validar com sucesso uma signature do hubspot de versao v1")
    void shouldValidateSignatureV1Success() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = clientSecret.concat(objectMapper.writeValueAsString(list));
        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Assertions.assertDoesNotThrow(
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v1", null));

    }

    @Test
    @DisplayName(value = "Deve lançar uma WebhookInvalidSignatureException quando a validação falhar para signature da versão v1")
    void shouldThrowWebhookInvalidSignatureExceptionWhenInvalidSignatureV1() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = "invalid".concat(objectMapper.writeValueAsString(list));
        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Throwable throwable = Assertions.assertThrows(WebhookInvalidSignatureException.class,
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v1", null));

        Assertions.assertNotNull(throwable);
        Assertions.assertEquals(WebhookInvalidSignatureException.class, throwable.getClass());
    }

    @Test
    @DisplayName(value = "Deve validar com sucesso uma signature do hubspot de versao v2")
    void shouldValidateSignatureV2Success() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = clientSecret.concat("POST").concat("http://localhost/test").concat(objectMapper.writeValueAsString(list));
        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Assertions.assertDoesNotThrow(
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v2", null));

    }

    @Test
    @DisplayName(value = "Deve lançar uma WebhookInvalidSignatureException quando a validação falhar para signature da versão v2")
    void shouldThrowWebhookInvalidSignatureExceptionWhenInvalidSignatureV2() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = clientSecret.concat("GET").concat("http://localhost/test").concat(objectMapper.writeValueAsString(list));
        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Throwable throwable = Assertions.assertThrows(WebhookInvalidSignatureException.class,
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v2", null));

        Assertions.assertNotNull(throwable);
        Assertions.assertEquals(WebhookInvalidSignatureException.class, throwable.getClass());
    }

    @Test
    @DisplayName(value = "Deve validar com sucesso uma signature do hubspot de versao v3")
    void shouldValidateSignatureV3Success() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = clientSecret.concat("POST")
                .concat("http://localhost/test")
                .concat(objectMapper.writeValueAsString(list)
                        .concat("2025-03-16T20:30:30"));

        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Assertions.assertDoesNotThrow(
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v3", "2025-03-16T20:30:30"));

    }

    @Test
    @DisplayName(value = "Deve lançar uma WebhookInvalidSignatureException quando a validação falhar para signature da versão v3")
    void shouldThrowWebhookInvalidSignatureExceptionWhenInvalidSignatureV3() throws JsonProcessingException {

        List<CreatedContactWebhookRequestDTO> list = createWebhookRequestList();

        String sourceString = clientSecret.concat("GET")
                .concat("http://localhost/test")
                .concat(objectMapper.writeValueAsString(list)
                        .concat("2025-03-16T20:30:30"));

        String hash = hubspotContactWebhookService.generateSha256(sourceString);

        Throwable throwable = Assertions.assertThrows(WebhookInvalidSignatureException.class,
                () -> hubspotContactWebhookService.webhookContactCreation(list, hash, "v3", null));

        Assertions.assertNotNull(throwable);
        Assertions.assertEquals(WebhookInvalidSignatureException.class, throwable.getClass());
    }

    private List<CreatedContactWebhookRequestDTO> createWebhookRequestList() {
        return List.of(
                new CreatedContactWebhookRequestDTO(
                        1L,
                        "source",
                        1L,
                        1L,
                        1L,
                        1L,
                        1L,
                        "Contact.creation",
                        1L
                )
        );
    }
}
