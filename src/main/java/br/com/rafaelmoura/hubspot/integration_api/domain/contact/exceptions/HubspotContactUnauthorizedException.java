package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

public class HubspotContactUnauthorizedException extends RuntimeException {
    public HubspotContactUnauthorizedException(String message) {
        super(message);
    }
}
