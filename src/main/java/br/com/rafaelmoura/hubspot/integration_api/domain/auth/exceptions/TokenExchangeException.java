package br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions;

public class TokenExchangeException extends RuntimeException {
    public TokenExchangeException(String message) {
        super(message);
    }
}
