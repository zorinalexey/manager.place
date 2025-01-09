package lidofon.auth.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TokenAuthentication extends AbstractAuthenticationToken {
    private final String token;

    public TokenAuthentication(String token) {
        super(null);
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}