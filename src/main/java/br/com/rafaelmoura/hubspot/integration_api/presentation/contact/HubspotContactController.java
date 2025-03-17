package br.com.rafaelmoura.hubspot.integration_api.presentation.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.contact.HubspotContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/contacts")
@Slf4j
@RequiredArgsConstructor
public class HubspotContactController {

    private final HubspotContactService hubspotContactService;

    @PostMapping(value = "/v1")
    public ResponseEntity<HubspotContactResponseDTO> createContact(@Valid @RequestBody HubspotContactRequestDTO hubspotContactRequestDTO,
                                                                   @RequestHeader(name = "Authorization") String accessToken) {

        log.info("[HubspotContactController][createContact] - chamando HubspotContactService para criação de contato no hubspot. Email: [{}]",
                hubspotContactRequestDTO.properties().email());

        HubspotContactResponseDTO hubspotContactResponseDTO = hubspotContactService.createContact(hubspotContactRequestDTO, accessToken);

        log.info("[HubspotContactController][createContact] - chamada ao HubspotContactService para criação de contato no hubspot concluida. Email: [{}] ContactId: [{}]",
                hubspotContactRequestDTO.properties().email(), hubspotContactResponseDTO.contactId());

        return new ResponseEntity<>(hubspotContactResponseDTO, HttpStatus.CREATED);
    }
}