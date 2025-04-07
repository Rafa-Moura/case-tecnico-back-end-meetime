package br.com.rafaelmoura.hubspot.integration_api.application.mapper.auth;

import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.IntrospectTokenResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.application.dto.auth.response.TokenExchangeResponseDTO;
import br.com.rafaelmoura.hubspot.integration_api.domain.auth.entities.HubspotAuthUser;
import br.com.rafaelmoura.hubspot.integration_api.infrastructure.httpclient.vo.TokenExchangeResponseVO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HubspotAuthMapper {

    public TokenExchangeResponseDTO fromTokenExchangeResponseVO(TokenExchangeResponseVO tokenExchangeResponseVO, String user) {
        return new TokenExchangeResponseDTO(
                tokenExchangeResponseVO.accessToken(),
                tokenExchangeResponseVO.expiresIn(),
                tokenExchangeResponseVO.tokenType(),
                user
        );
    }

    public HubspotAuthUser  toHubspotAuthUser(TokenExchangeResponseVO tokenExchangeResponseVO,
                                              IntrospectTokenResponseDTO introspectTokenResponseDTO) {

        return HubspotAuthUser.builder()
                .user(introspectTokenResponseDTO.user())
                .refreshToken(tokenExchangeResponseVO.refreshToken())
                .userId(introspectTokenResponseDTO.userId())
                .build();
    }
}
