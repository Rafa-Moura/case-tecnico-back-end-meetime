package br.com.rafaelmoura.hubspot.integration_api.domain.contact.exceptions;

public class HubspotContactAlreadyExistsException extends RuntimeException {
  public HubspotContactAlreadyExistsException(String message) {
    super(message);
  }
}
