package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

public class HubspotContactBusinessException extends RuntimeException {
    public HubspotContactBusinessException(String message) {
        super(message);
    }
}
