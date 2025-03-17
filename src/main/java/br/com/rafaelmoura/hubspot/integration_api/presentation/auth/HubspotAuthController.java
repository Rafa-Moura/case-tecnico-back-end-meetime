package br.com.rafaelmoura.hubspot.integration_api.presentation.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.auth.HubspotAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth/hubspot")
@Slf4j
@RequiredArgsConstructor
public class HubspotAuthController {

    private final HubspotAuthService hubspotAuthService;

    @GetMapping(value = "/v1/authorization")
    public ResponseEntity<String> generateAuthorizationUri() {
        log.info("[HubspotAuthController][generateAuthorizationUri] - iniciando fluxo para gerar a url de autorização do Hubspot");

        String authorizationUri = hubspotAuthService.generateAuthorizationUri();

        log.info("[HubspotAuthController][generateAuthorizationUri] - finalizando fluxo para gerar a url de autorização do Hubspot");
        return new ResponseEntity<>(authorizationUri, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/token-exchange")
    public ResponseEntity<TokenExchangeResponseDTO> tokenExchange(@RequestParam String code) {
        log.info("[HubspotAuthController][tokenExchange] - iniciando chamada ao HubspotAuthService para troca de code por access token. Code: [{}]",
                code);

        TokenExchangeResponseDTO tokenExchangeResponseDTO = hubspotAuthService.tokenExchange(code);

        log.info("[HubspotAuthController][tokenExchange] - chamada ao HubspotAuthService para troca de code por access token concluída. Code: [{}]",
                code);
        return new ResponseEntity<>(tokenExchangeResponseDTO, HttpStatus.OK);
    }
}