package br.com.rafaelmoura.hubspot.integration_api.domain.webhook.exceptions;

public class WebhookInvalidSignatureException extends RuntimeException {
    public WebhookInvalidSignatureException(String message) {
        super(message);
    }
}
