package br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class HubspotAuthExceptionHandler {

    @ExceptionHandler(TokenExchangeException.class)
    public ResponseEntity<ErrorDTO> handleTokenExchangeException(TokenExchangeException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("TOKEN_EXCHANGE_ERROR")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HubspotAuthBusinessException.class)
    public ResponseEntity<ErrorDTO> handleHubspotAuthBusinessException(HubspotAuthBusinessException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message("HUBSPOT_AUTH_BUSINESS_EXCEPTION")
                .title(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HubspotAuthSystemException.class)
    public ResponseEntity<ErrorDTO> handleHubspotAuthSystemException(HubspotAuthSystemException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .message("HUBSPOT_AUTH_SYSTEM_EXCEPTION")
                .title(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}