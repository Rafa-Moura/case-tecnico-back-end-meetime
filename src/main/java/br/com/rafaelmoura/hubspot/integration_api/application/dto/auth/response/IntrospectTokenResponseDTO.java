package br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IntrospectTokenResponseDTO (
        String user,
        @JsonProperty("user_id")
        Integer userId,
        String token,
        List<String> scopes
){
}
