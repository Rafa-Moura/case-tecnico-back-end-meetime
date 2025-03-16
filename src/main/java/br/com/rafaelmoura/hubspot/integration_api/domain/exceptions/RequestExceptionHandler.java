package br.com.rafaelmoura.hubspot.integration_api.domain.exceptions;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("MISSING_PARAMETER_EXCEPTION")
                .message(String.format("erro durante a requisição. Parâmetro obrigatório: [%s] não informado ou inválido", ex.getParameterName()))
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
