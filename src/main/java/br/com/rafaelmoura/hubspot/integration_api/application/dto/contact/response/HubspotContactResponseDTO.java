package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "HubspotContactResponseDTO", description = "Objeto contendo ids do contato e demais informações")
public record HubspotContactResponseDTO(
        @Schema(description = "Id gerado no banco de dados para o contato", example = "1000")
        Long id,
        @Schema(description = "Id do objeto no Hubspot", example = "2000")
        String contactId,
        ContactPropertiesResponseDTO properties
) {
}