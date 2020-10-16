package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;

import static com.test.lei.tokenUtil.ParameterValidationUtils.validateNotEmpty;

/**
 * @description: ClientCredentialParameters
 * @author: leiming5
 * @date: 2020-10-09 10:17
 */
public class ClientCredentialParameters {
    @NonNull
    private Set<String> scopes;

    private static ClientCredentialParameters.ClientCredentialParametersBuilder builder() {
        return new ClientCredentialParameters.ClientCredentialParametersBuilder();
    }

    public static ClientCredentialParameters.ClientCredentialParametersBuilder builder(Set<String> scopes) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        return builder().scopes(scopes);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @ConstructorProperties({"scopes"})
    private ClientCredentialParameters(@NonNull Set<String> scopes) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
        }
    }

    public static class ClientCredentialParametersBuilder {
        private Set<String> scopes;

        ClientCredentialParametersBuilder() {
        }

        public ClientCredentialParameters.ClientCredentialParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public ClientCredentialParameters build() {
            return new ClientCredentialParameters(this.scopes);
        }

        @Override
        public String toString() {
            return "ClientCredentialParameters.ClientCredentialParametersBuilder(scopes=" + this.scopes + ")";
        }
    }
}
