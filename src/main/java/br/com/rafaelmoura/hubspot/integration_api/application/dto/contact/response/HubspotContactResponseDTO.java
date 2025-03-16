package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response;

public record HubspotContactResponseDTO(
        Long id,
        String contactId,
        ContactPropertiesResponseDTO properties
) {
}