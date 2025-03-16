package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;

public interface HubspotContactClient {

    HubspotContactResponseVO createContact(HubspotContactRequestDTO hubspotContactRequestDTO, String accessToken);

}
