package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

public class HubspotContactSystemException extends RuntimeException {
    public HubspotContactSystemException(String message) {
        super(message);
    }
}
