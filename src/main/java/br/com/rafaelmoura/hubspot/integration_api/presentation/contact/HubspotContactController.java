package br.com.rafaelmoura.hubspot.integration_api.presentation.contact;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request.HubspotContactRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response.HubspotContactResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.contact.HubspotContactService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/contacts")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Hubspot Contact Controller", description = "Controller para criação de contatos no Hubspot")
public class HubspotContactController {

    private final HubspotContactService hubspotContactService;

    @PostMapping(value = "/v1")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Created. Indica que o Contato foi criado com sucesso",
                    responseCode = "201",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = HubspotContactResponseDTO.class))
            ),
            @ApiResponse(
                    description = "Erro na requisição. Indica recursos necessários ausentes ou mal formatados",
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Não autorizado. Indica que a operação para criar novo contato não foi autorizada pelo Hubspot",
                    responseCode = "401",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Conflito. Indica que o contato informado já existe no Hubspot",
                    responseCode = "409",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Limite de requisições atingido. Indica que o número de requisições diárias permitidas para o Hubspot foi atingido.",
                    responseCode = "429",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Erro interno. Indica erro interno no processamento da requisição",
                    responseCode = "500",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
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