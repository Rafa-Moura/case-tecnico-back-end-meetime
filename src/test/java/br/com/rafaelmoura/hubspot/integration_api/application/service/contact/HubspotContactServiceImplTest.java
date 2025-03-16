package br.com.rafaelmoura.hubspot.integration_api.application.service.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.ContactPropertiesRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.entities.HubspotContact;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.repository.HubspotContactRepository;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.HubspotContactClient;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.ContactPropertiesResponseVO;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HubspotContactServiceImplTest {

    @Mock
    HubspotContactRepository hubspotContactRepository;

    @Mock
    HubspotContactClient hubspotContactClient;

    @InjectMocks
    HubspotContactServiceImpl hubspotContactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(value = "Deve chamar o hubspotContactClient, HubspotContactRepository e retornar um HubspotContactResponseDTO")
    void shouldCreateHubspotContactSuccess() {

        HubspotContact hubspotContact = new HubspotContact(1L, "1234", "rafael", "moura", "rf@gmail.com");

        HubspotContactResponseVO hubspotContactResponseVO = new HubspotContactResponseVO(
                "1", new ContactPropertiesResponseVO("rf@gmail.com", "rafael", "moura")
        );

        HubspotContactRequestDTO hubspotContactRequestDTO = new HubspotContactRequestDTO(
                new ContactPropertiesRequestDTO("rf@gmail.com", "rafael", "moura")
        );

        Mockito.when(hubspotContactRepository.save(Mockito.any())).thenReturn(hubspotContact);
        Mockito.when(hubspotContactClient.createContact(Mockito.any(), Mockito.anyString())).thenReturn(hubspotContactResponseVO);

        HubspotContactResponseDTO hubspotContactResponseDTO = hubspotContactService
                .createContact(hubspotContactRequestDTO, "access-token");

        Assertions.assertNotNull(hubspotContactResponseDTO);
        Assertions.assertNotNull(hubspotContactResponseDTO.properties());
        Assertions.assertEquals(1L, hubspotContactResponseDTO.id());
        Assertions.assertEquals("1234", hubspotContactResponseDTO.contactId());
        Assertions.assertEquals("rafael", hubspotContactResponseDTO.properties().firstname());
        Assertions.assertEquals("moura", hubspotContactResponseDTO.properties().lastname());
        Assertions.assertEquals("rf@gmail.com", hubspotContactResponseDTO.properties().email());

        Mockito.verify(hubspotContactRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(hubspotContactClient, Mockito.times(1)).createContact(Mockito.any(), Mockito.anyString());
    }
}
