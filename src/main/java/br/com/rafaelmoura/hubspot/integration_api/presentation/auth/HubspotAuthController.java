package br.com.rafaelmoura.hubspot.integration_api.presentation.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions.ErrorDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.service.auth.HubspotAuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth/hubspot")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Hubspot Auth Controller", description = "Controller para fluxo Oauth2 do Hubspot")
public class HubspotAuthController {

    private final HubspotAuthService hubspotAuthService;

    @GetMapping(value = "/v1/authorization")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "OK. Indica que a uri de autorização foi gerada com sucesso",
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    description = "Erro interno. Indica erro interno no processamento da requisição",
                    responseCode = "500",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<String> generateAuthorizationUri() {
        log.info("[HubspotAuthController][generateAuthorizationUri] - iniciando fluxo para gerar a url de autorização do Hubspot");

        String authorizationUri = hubspotAuthService.generateAuthorizationUri();

        log.info("[HubspotAuthController][generateAuthorizationUri] - finalizando fluxo para gerar a url de autorização do Hubspot");
        return new ResponseEntity<>(authorizationUri, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/token-exchange")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "OK. Indica que o processo de token exchange foi realizado com sucesso",
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenExchangeResponseDTO.class))
            ),
            @ApiResponse(
                    description = "Erro na requisição. Indica recursos necessários ausentes ou mal formatados",
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Não autorizado. Indica que a operação de token exchange não foi autorizada pelo Hubspot",
                    responseCode = "401",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    description = "Erro interno. Indica erro interno no processamento da requisição",
                    responseCode = "500",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<TokenExchangeResponseDTO> tokenExchange(@RequestParam String code) {
        log.info("[HubspotAuthController][tokenExchange] - iniciando chamada ao HubspotAuthService para troca de code por access token. Code: [{}]",
                code);

        TokenExchangeResponseDTO tokenExchangeResponseDTO = hubspotAuthService.tokenExchange(code);

        log.info("[HubspotAuthController][tokenExchange] - chamada ao HubspotAuthService para troca de code por access token concluída. Code: [{}]",
                code);
        return new ResponseEntity<>(tokenExchangeResponseDTO, HttpStatus.OK);
    }
}