package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;

/**
 * @description: IntegratedWindowsAuthenticationParameters
 * @author: leiming5
 * @date: 2020-10-14 10:50
 */
public class IntegratedWindowsAuthenticationParameters {
    @NonNull
    private Set<String> scopes;
    @NonNull
    private String username;

    private static IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder builder() {
        return new IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder();
    }

    public static IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder builder(Set<String> scopes, String username) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        ParameterValidationUtils.validateNotBlank("username", username);
        return builder().scopes(scopes).username(username);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public String username() {
        return this.username;
    }

    @ConstructorProperties({"scopes", "username"})
    private IntegratedWindowsAuthenticationParameters(@NonNull Set<String> scopes, @NonNull String username) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else if (username == null) {
            throw new NullPointerException("username is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.username = username;
        }
    }

    public static class IntegratedWindowsAuthenticationParametersBuilder {
        private Set<String> scopes;
        private String username;

        IntegratedWindowsAuthenticationParametersBuilder() {
        }

        public IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder username(String username) {
            this.username = username;
            return this;
        }

        public IntegratedWindowsAuthenticationParameters build() {
            return new IntegratedWindowsAuthenticationParameters(this.scopes, this.username);
        }

        public String toString() {
            return "IntegratedWindowsAuthenticationParameters.IntegratedWindowsAuthenticationParametersBuilder(scopes=" + this.scopes + ", username=" + this.username + ")";
        }
    }
}
