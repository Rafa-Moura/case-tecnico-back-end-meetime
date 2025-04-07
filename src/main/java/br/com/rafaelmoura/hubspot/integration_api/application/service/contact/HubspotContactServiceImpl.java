package br.com.rafaelmoura.hubspot.integration_api.application.service.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.mapper.contact.HubspotContactMapper;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.entities.HubspotContact;
import br.com.rafaelmoura.hubspot.integration_api.domain.contact.repository.HubspotContactRepository;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.HubspotContactClient;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo.HubspotContactResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubspotContactServiceImpl implements HubspotContactService{

    private final HubspotContactRepository hubspotContactRepository;
    private final HubspotContactClient hubspotContactClient;

    @Override
    public HubspotContactResponseDTO createContact(HubspotContactRequestDTO hubspotContactRequestDTO, String accessToken) {

        log.info("[HubspotContactServiceImpl][createContact] - chamando HubspotContactClient para criar contato no hubspot. Email: [{}]", hubspotContactRequestDTO.properties().email());

        HubspotContactResponseVO hubspotContactResponseVO = hubspotContactClient
                .createContact(hubspotContactRequestDTO, accessToken);

        HubspotContact createdContact = hubspotContactRepository
                .save(HubspotContactMapper.fromHubspotCreateContactResponseVO(hubspotContactResponseVO));

        log.info("[HubspotContactServiceImpl][createContact] - chamada ao HubspotContactClient para criar contato no hubspot concluida. Email: [{}] ContacId: [{}]",
                createdContact.getEmail(), createdContact.getContactId());

        return HubspotContactMapper.fromHubspotContact(createdContact);
    }
}