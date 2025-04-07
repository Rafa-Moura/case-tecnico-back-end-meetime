package br.com.rafaelmoura.hubspot.integration_api.application.mapper.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.ContactPropertiesResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.entities.HubspotContact;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HubspotContactMapper {

    public HubspotContact fromHubspotCreateContactResponseVO(HubspotContactResponseVO hubspotContactResponseVO) {

        return HubspotContact.builder()
                .contactId(hubspotContactResponseVO.id())
                .email(hubspotContactResponseVO.properties().email())
                .firstname(hubspotContactResponseVO.properties().firstname())
                .lastname(hubspotContactResponseVO.properties().lastname())
                .build();
    }

    public HubspotContactResponseDTO fromHubspotContact(HubspotContact hubspotContact) {

        return new HubspotContactResponseDTO(
                hubspotContact.getId(),
                hubspotContact.getContactId(),
                new ContactPropertiesResponseDTO(
                        hubspotContact.getEmail(),
                        hubspotContact.getFirstname(),
                        hubspotContact.getLastname()
                )
        );
    }
}