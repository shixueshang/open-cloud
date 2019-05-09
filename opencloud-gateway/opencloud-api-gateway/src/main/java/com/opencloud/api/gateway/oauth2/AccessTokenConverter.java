package com.opencloud.api.gateway.oauth2;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author liuyadu
 */
public class AccessTokenConverter implements Converter<OAuth2AccessToken, AbstractAuthenticationToken> {
    private static final String SCOPE_AUTHORITY_PREFIX = "SCOPE_";


	@Override
	public final AbstractAuthenticationToken convert(OAuth2AccessToken auth2AccessToken) {
		Collection<GrantedAuthority> authorities = extractAuthorities(auth2AccessToken);
		return new Oauth2AuthenticationToken(auth2AccessToken, authorities);
	}

	protected Collection<GrantedAuthority> extractAuthorities(OAuth2AccessToken auth2AccessToken) {
		return auth2AccessToken.getScopes()
						.stream()
						.map(authority -> SCOPE_AUTHORITY_PREFIX + authority)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
	}

}