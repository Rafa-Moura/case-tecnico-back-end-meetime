package br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "CreatedContactWebhookRequestDTO", description = "Objeto que contém dados do evento Contat.creation do hubspot")
public record CreatedContactWebhookRequestDTO(
        @Schema(description = "Id do objeto criado", example = "100000")
        Long objectId,
        @Schema(description = "Recurso alterado", example = "RECURSO")
        String changeSource,
        @Schema(description = "Id do evento", example = "100000")
        Long eventId,
        @Schema(description = "Id da subscrição", example = "100000")
        Long subscriptionId,
        @Schema(description = "Id do portal", example = "100000")
        Long portalId,
        @Schema(description = "Id da aplicação", example = "100000")
        Long appId,
        @Schema(description = "Timestamp de quando ocorreu o evento", example = "100000")
        Long occurredAt,
        @Schema(description = "Tipo do evento", example = "Contact.creation")
        String eventType,
        @Schema(description = "Número de tentativas", example = "100")
        Long attemptNumber
) {
}