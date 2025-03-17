package br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDTO {

    private String title;
    private String message;
    private String requestDateTime;

}
