package br.com.rafaelmoura.hubspot.integration_api.application.dto.contact.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ContactPropertiesResponseDTO", description = "Objeto com dados do usuário criado")
public record ContactPropertiesResponseDTO(
        @Schema(description = "Email do contato", example = "teste@gmail.com")
        String email,
        @Schema(description = "Primeiro nome do contato", example = "João")
        String firstname,
        @Schema(description = "Último nome do contato", example = "Ferraz")
        String lastname
){
}
