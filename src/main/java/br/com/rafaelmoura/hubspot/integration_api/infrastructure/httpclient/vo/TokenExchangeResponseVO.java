package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenExchangeResponseVO(
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