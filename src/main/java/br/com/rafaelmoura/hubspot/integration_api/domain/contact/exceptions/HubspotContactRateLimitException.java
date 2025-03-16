package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

public class HubspotContactRateLimitException extends RuntimeException {
    public HubspotContactRateLimitException(String message) {
        super(message);
    }
}
