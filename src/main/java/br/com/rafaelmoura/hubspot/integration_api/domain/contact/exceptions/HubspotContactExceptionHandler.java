package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class HubspotContactExceptionHandler {

    @ExceptionHandler(HubspotContactAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleHubspotContactAlreadyExists(HubspotContactAlreadyExistsException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("CONTACT_ALREADY_EXISTS")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HubspotContactBusinessException.class)
    public ResponseEntity<ErrorDTO> handleHubspotContactBusinessException(HubspotContactBusinessException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("CONTACT_BUSINESS_EXCEPTION")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HubspotContactUnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleHubspotContactUnauthorizedException(HubspotContactUnauthorizedException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("CONTACT_UNAUTHORIZED_EXCEPTION")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HubspotContactSystemException.class)
    public ResponseEntity<ErrorDTO> handleHubspotContactSystemException(HubspotContactSystemException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("CONTACT_SYSTEM_EXCEPTION")
                .message("Ocorreu um erro interno. Contate um administrador do sistema para maiores informações")
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HubspotContactRateLimitException.class)
    public ResponseEntity<ErrorDTO> handleHubspotContactRateLimitException(HubspotContactRateLimitException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("CONTACT_RATE_LIMIT_EXCEPTION")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.TOO_MANY_REQUESTS);
    }
}