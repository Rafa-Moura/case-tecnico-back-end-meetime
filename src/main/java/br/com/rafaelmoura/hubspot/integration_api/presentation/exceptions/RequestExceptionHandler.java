package br.com.rafaelmoura.hubspot.integration_api.presentation.exceptions;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorDTO> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .title("MISSING_HEADER_EXCEPTION")
                .message(ex.getMessage())
                .requestDateTime(LocalDateTime.now().toString())
                .build();

        if (ex.getHeaderName().equals("Authorization")) {
            errorDTO.setMessage("Header de autorização invalido ou não informado");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<ErrorDTO> listErrors = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();

        for (var field : ex.getFieldErrors()) {
            listErrors.add(ErrorDTO.builder()
                    .title(field.getField())
                    .message(field.getDefaultMessage())
                    .requestDateTime(localDateTime.toString())
                    .build());
        }

        return new ResponseEntity<>(listErrors, HttpStatus.BAD_REQUEST);
    }
}
