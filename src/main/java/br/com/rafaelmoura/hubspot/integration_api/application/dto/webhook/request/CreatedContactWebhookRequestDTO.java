package br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatedContactWebhookRequestDTO(
        Long objectId,
        String changeSource,
        Long eventId,
        Long subscriptionId,
        Long portalId,
        Long appId,
        Long occurredAt,
        String eventType,
        Long attemptNumber
) {
}