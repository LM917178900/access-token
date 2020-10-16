package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;

/**
 * @description: OnBehalfOfParameters
 * @author: leiming5
 * @date: 2020-10-15 18:58
 */
public class OnBehalfOfParameters {
    @NonNull
    private Set<String> scopes;
    @NonNull
    private IUserAssertion userAssertion;

    private static OnBehalfOfParameters.OnBehalfOfParametersBuilder builder() {
        return new OnBehalfOfParameters.OnBehalfOfParametersBuilder();
    }

    public static OnBehalfOfParameters.OnBehalfOfParametersBuilder builder(Set<String> scopes, UserAssertion userAssertion) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        return builder().scopes(scopes).userAssertion(userAssertion);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public IUserAssertion userAssertion() {
        return this.userAssertion;
    }

    @ConstructorProperties({"scopes", "userAssertion"})
    private OnBehalfOfParameters(@NonNull Set<String> scopes, @NonNull IUserAssertion userAssertion) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else if (userAssertion == null) {
            throw new NullPointerException("userAssertion is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.userAssertion = userAssertion;
        }
    }

    public static class OnBehalfOfParametersBuilder {
        private Set<String> scopes;
        private IUserAssertion userAssertion;

        OnBehalfOfParametersBuilder() {
        }

        public OnBehalfOfParameters.OnBehalfOfParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public OnBehalfOfParameters.OnBehalfOfParametersBuilder userAssertion(IUserAssertion userAssertion) {
            this.userAssertion = userAssertion;
            return this;
        }

        public OnBehalfOfParameters build() {
            return new OnBehalfOfParameters(this.scopes, this.userAssertion);
        }

        public String toString() {
            return "OnBehalfOfParameters.OnBehalfOfParametersBuilder(scopes=" + this.scopes + ", userAssertion=" + this.userAssertion + ")";
        }
    }
}

