package br.com.rafaelmoura.hubspot.integration_api.domain.exceptions;

public class SystemException extends RuntimeException {
    public SystemException(String message) {
        super(message);
    }
}
