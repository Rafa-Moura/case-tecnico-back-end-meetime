package br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenExchangeResponseDTO(
        @JsonProperty(value = "access_token")
        String accessToken,
        @JsonProperty(value = "expires_in")
        Integer expiresIn,
        @JsonProperty(value = "token_type")
        String tokenType,
        @JsonProperty(value = "refresh_token")
        String refreshToken
) {
}