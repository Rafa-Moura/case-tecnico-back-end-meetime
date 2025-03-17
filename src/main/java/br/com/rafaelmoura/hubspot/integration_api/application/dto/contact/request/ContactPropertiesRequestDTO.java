package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ContactPropertiesRequestDTO", description = "Objeto contendo dados para criação do contato")
public record ContactPropertiesRequestDTO(
        @NotBlank(message = "campo obrigatório")
        @Email(message = "favor preencher com um email valido")
        @Schema(description = "Email do contato", example = "teste@gmail.com")
        String email,
        @NotBlank(message = "campo obrigatório")
        @Schema(description = "Primeiro nome do contato", example = "João")
        String firstname,
        @NotBlank(message = "campo obrigatório")
        @Schema(description = "Último nome do contato", example = "Ferraz")
        String lastname
) {
}
