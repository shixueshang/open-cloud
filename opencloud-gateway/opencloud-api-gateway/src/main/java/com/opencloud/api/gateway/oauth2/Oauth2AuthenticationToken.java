package com.opencloud.api.gateway.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;

@Transient
public class Oauth2AuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<OAuth2AccessToken> {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


	public Oauth2AuthenticationToken(OAuth2AccessToken accessToken) {
		super(accessToken);
	}

	public Oauth2AuthenticationToken(OAuth2AccessToken accessToken , Collection<? extends GrantedAuthority> authorities) {
		super(accessToken, authorities);
		this.setAuthenticated(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getTokenAttributes() {
		return null;
	}


	@Override
	public String getName() {
		return this.getToken().getTokenValue();
	}
}