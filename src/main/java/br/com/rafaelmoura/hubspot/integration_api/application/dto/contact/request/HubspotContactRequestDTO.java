package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request;

import jakarta.validation.Valid;

public record HubspotContactRequestDTO(
        @Valid
        ContactPropertiesRequestDTO properties
) {
}
