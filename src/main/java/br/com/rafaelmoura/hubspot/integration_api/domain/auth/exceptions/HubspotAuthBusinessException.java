package br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions;

public class HubspotAuthBusinessException extends RuntimeException {
    public HubspotAuthBusinessException(String message) {
        super(message);
    }
}
