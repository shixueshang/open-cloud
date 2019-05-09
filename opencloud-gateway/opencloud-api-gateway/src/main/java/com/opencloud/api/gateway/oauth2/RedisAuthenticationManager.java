package com.opencloud.api.gateway.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import reactor.core.publisher.Mono;

/**
 * @author: liuyadu
 * @date: 2019/5/9 10:53
 * @description:
 */
@Slf4j
public class RedisAuthenticationManager implements ReactiveAuthenticationManager {
    TokenStore tokenStore;

    public RedisAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.debug("===========>");
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((token->{
                    OAuth2AccessToken oAuth2AccessToken =  this.tokenStore.readAccessToken(token);
                    log.info("==={}",oAuth2AccessToken);
                    return Mono.justOrEmpty(oAuth2AccessToken);
                }))
                .cast(Authentication.class)
                .onErrorMap(InvalidTokenException.class, this::onError);
    }

    private OAuth2AuthenticationException onError(InvalidTokenException e) {
        OAuth2Error invalidRequest = invalidToken(e.getMessage());
        return new OAuth2AuthenticationException(invalidRequest, e.getMessage());
    }

    private static OAuth2Error invalidToken(String message) {
        return new BearerTokenError(
                BearerTokenErrorCodes.INVALID_TOKEN,
                HttpStatus.UNAUTHORIZED,
                message,
                "https://tools.ietf.org/html/rfc6750#section-3.1");
    }

    public TokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
}
