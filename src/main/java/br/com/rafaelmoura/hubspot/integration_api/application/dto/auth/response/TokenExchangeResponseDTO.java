package br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "TokenExchangeResponseDTO", description = "Objeto com dados do retorno do processo de token_exchange")
public record TokenExchangeResponseDTO(
        @JsonProperty(value = "access_token")
        @Schema(description = "Access token para requisições no hubspot", example = "AAAAEeeerrr333333rrrr")
        String accessToken,
        @JsonProperty(value = "expires_in")
        @Schema(description = "Tempo de expiração do access token", example = "1800")
        Integer expiresIn,
        @JsonProperty(value = "token_type")
        @Schema(description = "Tipo do access token", example = "Bearer")
        String tokenType,
        @JsonProperty(value = "user")
        @Schema(description = "Email do usuário logado no Hubspot", example = "rfmoura@gmail.com")
        String user
) {
}