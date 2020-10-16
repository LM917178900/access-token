package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;

/**
 * @description: RefreshTokenParameters
 * @author: leiming5
 * @date: 2020-10-15 18:31
 */
public class RefreshTokenParameters {
    @NonNull
    private Set<String> scopes;
    @NonNull
    private String refreshToken;

    private static RefreshTokenParameters.RefreshTokenParametersBuilder builder() {
        return new RefreshTokenParameters.RefreshTokenParametersBuilder();
    }

    public static RefreshTokenParameters.RefreshTokenParametersBuilder builder(Set<String> scopes, String refreshToken) {
        ParameterValidationUtils.validateNotBlank("refreshToken", refreshToken);
        return builder().scopes(scopes).refreshToken(refreshToken);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public String refreshToken() {
        return this.refreshToken;
    }

    @ConstructorProperties({"scopes", "refreshToken"})
    private RefreshTokenParameters(@NonNull Set<String> scopes, @NonNull String refreshToken) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else if (refreshToken == null) {
            throw new NullPointerException("refreshToken is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.refreshToken = refreshToken;
        }
    }

    public static class RefreshTokenParametersBuilder {
        private Set<String> scopes;
        private String refreshToken;

        RefreshTokenParametersBuilder() {
        }

        public RefreshTokenParameters.RefreshTokenParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public RefreshTokenParameters.RefreshTokenParametersBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenParameters build() {
            return new RefreshTokenParameters(this.scopes, this.refreshToken);
        }

        public String toString() {
            return "RefreshTokenParameters.RefreshTokenParametersBuilder(scopes=" + this.scopes + ", refreshToken=" + this.refreshToken + ")";
        }
    }
}
