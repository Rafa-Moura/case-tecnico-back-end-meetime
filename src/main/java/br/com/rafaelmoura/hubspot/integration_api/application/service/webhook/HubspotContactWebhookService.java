package br.com.rafaelmoura.hubspot.integration_api.application.service.webhook;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.webhook.request.CreatedContactWebhookRequestDTO;

import java.util.List;

public interface HubspotContactWebhookService {
    void webhookContactCreation(List<CreatedContactWebhookRequestDTO> requestDTO,
                                String webhookSignature,
                                String webhookSignatureVersion,
                                String webhookRequestTimestamp);
}