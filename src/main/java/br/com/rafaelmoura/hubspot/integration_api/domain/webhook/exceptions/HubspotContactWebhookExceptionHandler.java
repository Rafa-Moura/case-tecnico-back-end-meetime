package br.com.rafaelmoura.hubspot.integration_api.domain.webhook.exceptions;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class HubspotContactWebhookExceptionHandler {

    @ExceptionHandler(WebhookInvalidSignatureException.class)
    public ResponseEntity<ErrorDTO> handleWebhookInvalidSignatureException(WebhookInvalidSignatureException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("WEBHOOK_INVALID_SIGNATURE")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }
}
