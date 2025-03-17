package br.com.rafaelmoura.hubspot.integration_api.presentation.webhook;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request.CreatedContactWebhookRequestDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.webhook.HubspotContactWebhookService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/webhook/contacts")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Hubspot Contact Webhook Controller", description = "Controller que escuta os eventos Contact.creation do Hubspot")
public class HubspotContactWebhookController {

    private final HubspotContactWebhookService contactWebhookService;

    @PostMapping(value = "/v1")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Created. Indica que o webhook foi recebido e processado",
                    responseCode = "201"
            ),
            @ApiResponse(
                    description = "Erro na requisição. Indica recursos necessários ausentes ou mal formatados",
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Não autorizado. Indica que os dados de assinatura informados estão incorretos",
                    responseCode = "401",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Erro interno. Indica erro interno no processamento da requisição",
                    responseCode = "500",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public void webhookContactCreation(@Valid @RequestBody List<CreatedContactWebhookRequestDTO> requestDTO,
                                                    @RequestHeader(name = "X-HubSpot-Signature") String webhookSignature,
                                                    @RequestHeader(name = "X-HubSpot-Signature-Version") String webhookSignatureVersion,
                                                    @RequestHeader(name = "X-HubSpot-Request-Timestamp", required = false) String webhookRequestTimestamp) {

        log.info("[HubspotContactWebhookController][webhookContactCreation] - Evento de criação de contato recebido.");

        contactWebhookService.webhookContactCreation(requestDTO, webhookSignature, webhookSignatureVersion, webhookRequestTimestamp);

        log.info("[HubspotContactWebhookController][webhookContactCreation] - Evento de criação de contato concluido.");
    }
}