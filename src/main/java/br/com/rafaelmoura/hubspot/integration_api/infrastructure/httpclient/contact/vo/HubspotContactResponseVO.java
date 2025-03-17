package br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.contact.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HubspotContactResponseVO(
        String id,
        ContactPropertiesResponseVO properties
) {
}