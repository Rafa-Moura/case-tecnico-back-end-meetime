package br.com.rafaelmoura.hubspot.integration_api.presentation.exceptions;

public class SystemException extends RuntimeException {
    public SystemException(String message) {
        super(message);
    }
}
