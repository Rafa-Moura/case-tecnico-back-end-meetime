package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactPropertiesRequestDTO(
        @NotBlank(message = "campo obrigatório")
        @Email(message = "favor preencher com um email valido")
        String email,
        @NotBlank(message = "campo obrigatório")
        String firstname,
        @NotBlank(message = "campo obrigatório")
        String lastname
) {
}
