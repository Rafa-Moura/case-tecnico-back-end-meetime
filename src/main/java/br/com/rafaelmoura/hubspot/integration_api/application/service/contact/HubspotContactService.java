package br.com.rafaelmoura.hubspot.integration_api.application.service.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;

public interface HubspotContactService {
    HubspotContactResponseDTO createContact(HubspotContactRequestDTO hubspotContactRequestDTO, String accessToken);
}
