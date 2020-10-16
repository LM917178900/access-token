package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.Set;

/**
 * @description: AuthorizationCodeParameters
 * @author: leiming5
 * @date: 2020-10-15 18:30
 */
public class AuthorizationCodeParameters {
    private Set<String> scopes;
    @NonNull
    private String authorizationCode;
    @NonNull
    private URI redirectUri;

    private static AuthorizationCodeParameters.AuthorizationCodeParametersBuilder builder() {
        return new AuthorizationCodeParameters.AuthorizationCodeParametersBuilder();
    }

    public static AuthorizationCodeParameters.AuthorizationCodeParametersBuilder builder(String authorizationCode, URI redirectUri) {
        ParameterValidationUtils.validateNotBlank("authorizationCode", authorizationCode);
        return builder().authorizationCode(authorizationCode).redirectUri(redirectUri);
    }

    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public String authorizationCode() {
        return this.authorizationCode;
    }

    @NonNull
    public URI redirectUri() {
        return this.redirectUri;
    }

    @ConstructorProperties({"scopes", "authorizationCode", "redirectUri"})
    private AuthorizationCodeParameters(Set<String> scopes, @NonNull String authorizationCode, @NonNull URI redirectUri) {
        if (authorizationCode == null) {
            throw new NullPointerException("authorizationCode is marked @NonNull but is null");
        } else if (redirectUri == null) {
            throw new NullPointerException("redirectUri is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.authorizationCode = authorizationCode;
            this.redirectUri = redirectUri;
        }
    }

    public static class AuthorizationCodeParametersBuilder {
        private Set<String> scopes;
        private String authorizationCode;
        private URI redirectUri;

        AuthorizationCodeParametersBuilder() {
        }

        public AuthorizationCodeParameters.AuthorizationCodeParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public AuthorizationCodeParameters.AuthorizationCodeParametersBuilder authorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
            return this;
        }

        public AuthorizationCodeParameters.AuthorizationCodeParametersBuilder redirectUri(URI redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public AuthorizationCodeParameters build() {
            return new AuthorizationCodeParameters(this.scopes, this.authorizationCode, this.redirectUri);
        }

        @Override
        public String toString() {
            return "AuthorizationCodeParameters.AuthorizationCodeParametersBuilder(scopes=" + this.scopes + ", authorizationCode=" + this.authorizationCode + ", redirectUri=" + this.redirectUri + ")";
        }
    }
}
