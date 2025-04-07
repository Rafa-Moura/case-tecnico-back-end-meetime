package br.com.rafaelmoura.hubspot.integration_api.application.service.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.IntrospectTokenResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.mapper.auth.HubspotAuthMapper;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.entities.HubspotAuthUser;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.exceptions.TokenExchangeException;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.repository.HubspotAuthUserRepository;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.auth.HubspotAuthClient;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo.TokenExchangeResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubspotAuthServiceImpl implements HubspotAuthService{

    private final HubspotAuthClient hubspotAuthClient;
    private final HubspotAuthUserRepository hubspotAuthUserRepository;

    @Override
    public String generateAuthorizationUri() {
        log.info("[HubspotAuthServiceImpl][generateAuthorizationUri] - iniciando fluxo para gerar uri de autorização no HubspotAuthClient");

        String authorizationUri = hubspotAuthClient.generateAuthorizationUri();

        log.info("[HubspotAuthServiceImpl][generateAuthorizationUri] - fluxo para gerar uri de autorização no HubspotAuthClient finalizado");
        return authorizationUri;
    }

    @Override
    public TokenExchangeResponseDTO tokenExchange(String code) {
        log.info("[HubspotAuthServiceImpl][tokenExchange] - iniciando fluxo de token exchange no HubspotAuthClient. Code: [{}]", code);

        if (code.isBlank()) {
            log.error("[HubspotAuthServiceImpl][tokenExchange] - code informado para o token exchange é invalido");
            throw new TokenExchangeException("code informado para o token exchange é invalido");
        }

        TokenExchangeResponseVO tokenExchangeResponseVO = hubspotAuthClient.tokenExchange(code);

        IntrospectTokenResponseDTO introspectTokenResponse = hubspotAuthClient.introspectToken(tokenExchangeResponseVO.accessToken());

        HubspotAuthUser hubspotAuthUser = HubspotAuthMapper.toHubspotAuthUser(tokenExchangeResponseVO, introspectTokenResponse);

        hubspotAuthUserRepository.save(hubspotAuthUser);

        log.info("[HubspotAuthServiceImpl][tokenExchange] - fluxo de token exchange no HubspotAuthClient finalizado. Code: [{}]", code);
        return HubspotAuthMapper.fromTokenExchangeResponseVO(tokenExchangeResponseVO, introspectTokenResponse.user());
    }
}