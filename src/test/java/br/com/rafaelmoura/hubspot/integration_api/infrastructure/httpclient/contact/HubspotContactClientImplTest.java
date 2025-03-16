package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.ContactPropertiesRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions.*;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class HubspotContactClientImplTest {

    @Mock
    HttpClient httpClient;

    @Mock
    HttpResponse<Object> httpResponse;

    @InjectMocks
    HubspotContactClientImpl hubspotContactClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(hubspotContactClient, "hubspotContactUri", "http://hubtest.com/api/objects/contacts");
        ReflectionTestUtils.setField(hubspotContactClient, "objectMapper", new ObjectMapper());
    }

    @Test
    @DisplayName(value = "Deve criar com sucesso um novo contato no hubspot")
    void shouldCreateContactSuccess() throws IOException, InterruptedException {

        String responseBody = "{" +
                "    \"id\": \"106353655926\"," +
                "    \"properties\": {" +
                "        \"email\": \"rafael122@teste.com\"," +
                "        \"firstname\": \"Rafael\"," +
                "        \"lastname\": \"Moura\"" +
                "    }" +
                "}";

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(responseBody);

        HubspotContactResponseVO hubspotContactResponseVO =
                hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token");

        Assertions.assertNotNull(hubspotContactResponseVO);
        Assertions.assertNotNull(hubspotContactResponseVO.properties());

        Assertions.assertEquals("106353655926", hubspotContactResponseVO.id());
        Assertions.assertEquals("rafael122@teste.com", hubspotContactResponseVO.properties().email());
        Assertions.assertEquals("Rafael", hubspotContactResponseVO.properties().firstname());
        Assertions.assertEquals("Moura", hubspotContactResponseVO.properties().lastname());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar um HubspotContactUnauthorizedException quando o status da resposta do Hubspot for 401")
    void shouldThrowUnauthorizedExceptionWhenHubspotReturns401() throws IOException, InterruptedException {

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(401);

        Throwable unauthorizedError = Assertions.assertThrows(HubspotContactUnauthorizedException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "invalid-token"));

        Assertions.assertNotNull(unauthorizedError);
        Assertions.assertEquals(HubspotContactUnauthorizedException.class, unauthorizedError.getClass());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar um HubspotContactBusinessException quando o status da resposta do Hubspot for 400")
    void shouldThrowHubspotContactBusinessExceptionWhenHubspotReturns400() throws IOException, InterruptedException {

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(400);

        Throwable badRequestError = Assertions.assertThrows(HubspotContactBusinessException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token"));

        Assertions.assertNotNull(badRequestError);
        Assertions.assertEquals(HubspotContactBusinessException.class, badRequestError.getClass());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar um HubspotContactAlreadyExistsException quando o status da resposta do Hubspot for 409")
    void shouldThrowHubspotContactAlreadyExistsExceptionWhenHubspotReturns409() throws IOException, InterruptedException {

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(409);

        Throwable conflictError = Assertions.assertThrows(HubspotContactAlreadyExistsException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token"));

        Assertions.assertNotNull(conflictError);
        Assertions.assertEquals(HubspotContactAlreadyExistsException.class, conflictError.getClass());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar um HubspotContactSystemException quando ocorrer um IOException durante fluxo de criação de contato")
    void shouldThrowHubspotContactSystemExceptionWhenIOExceptionThrown() throws IOException, InterruptedException {
        String responseBody = "{";

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(responseBody);

        Throwable internalServerError = Assertions.assertThrows(HubspotContactSystemException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token"));

        Assertions.assertNotNull(internalServerError);
        Assertions.assertEquals(HubspotContactSystemException.class, internalServerError.getClass());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

    @Test
    @DisplayName(value = "Deve lançar um HubspotContactSystemException quando ocorrer um InterruptedException durante fluxo de criação de contato")
    void shouldThrownHubspotContactSystemExceptionWhenInterruptedExceptionThrown() throws IOException, InterruptedException {
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenThrow(new InterruptedException("Error"));

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Throwable internalServerError = Assertions.assertThrows(HubspotContactSystemException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token"));

        Assertions.assertNotNull(internalServerError);
        Assertions.assertEquals(HubspotContactSystemException.class, internalServerError.getClass());

        Mockito.verifyNoInteractions(httpResponse);
    }
    @Test
    @DisplayName(value = "Deve lançar um HubspotContactRateLimitException quando o status da resposta do Hubspot for 429")
    void shouldThrowHubspotContactRateLimitExceptionWhenHubspotReturns429() throws IOException, InterruptedException {

        HubspotContactRequestDTO hubspotContactRequestDTO =
                new HubspotContactRequestDTO(
                        new ContactPropertiesRequestDTO("rafael122@test.com", "Rafael", "Moura")
                );

        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(429);

        Throwable tooManyRequestsError = Assertions.assertThrows(HubspotContactRateLimitException.class,
                () -> hubspotContactClient.createContact(hubspotContactRequestDTO, "access-token"));

        Assertions.assertNotNull(tooManyRequestsError);
        Assertions.assertEquals(HubspotContactRateLimitException.class, tooManyRequestsError.getClass());

        Mockito.verify(httpResponse, Mockito.times(1)).statusCode();
        Mockito.verify(httpResponse, Mockito.times(1)).body();
    }

}
