package br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions;

public class HubspotAuthSystemException extends RuntimeException {
    public HubspotAuthSystemException(String message) {
        super(message);
    }
}
