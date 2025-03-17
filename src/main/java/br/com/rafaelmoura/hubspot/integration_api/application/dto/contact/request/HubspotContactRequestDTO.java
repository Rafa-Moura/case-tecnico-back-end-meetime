package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Schema(name = "HubspotContactRequestDTO", description = "Objeto que reune informações sobre o contato para criação")
public record HubspotContactRequestDTO(
        @Valid
        ContactPropertiesRequestDTO properties
) {
}
