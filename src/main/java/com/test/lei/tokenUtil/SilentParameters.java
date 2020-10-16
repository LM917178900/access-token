package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;

/**
 * @description: SilentParameters
 * @author: leiming5
 * @date: 2020-10-15 18:32
 */
public class SilentParameters {
    @NonNull
    private Set<String> scopes;
    private IAccount account;
    private String authorityUrl;
    private boolean forceRefresh;

    private static SilentParameters.SilentParametersBuilder builder() {
        return new SilentParameters.SilentParametersBuilder();
    }

    public static SilentParameters.SilentParametersBuilder builder(Set<String> scopes, IAccount account) {
        ParameterValidationUtils.validateNotNull("account", account);
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        return builder().scopes(scopes).account(account);
    }

    public static SilentParameters.SilentParametersBuilder builder(Set<String> scopes) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        return builder().scopes(scopes);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    public IAccount account() {
        return this.account;
    }

    public String authorityUrl() {
        return this.authorityUrl;
    }

    public boolean forceRefresh() {
        return this.forceRefresh;
    }

    @ConstructorProperties({"scopes", "account", "authorityUrl", "forceRefresh"})
    private SilentParameters(@NonNull Set<String> scopes, IAccount account, String authorityUrl, boolean forceRefresh) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.account = account;
            this.authorityUrl = authorityUrl;
            this.forceRefresh = forceRefresh;
        }
    }

    public static class SilentParametersBuilder {
        private Set<String> scopes;
        private IAccount account;
        private String authorityUrl;
        private boolean forceRefresh;

        SilentParametersBuilder() {
        }

        public SilentParameters.SilentParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public SilentParameters.SilentParametersBuilder account(IAccount account) {
            this.account = account;
            return this;
        }

        public SilentParameters.SilentParametersBuilder authorityUrl(String authorityUrl) {
            this.authorityUrl = authorityUrl;
            return this;
        }

        public SilentParameters.SilentParametersBuilder forceRefresh(boolean forceRefresh) {
            this.forceRefresh = forceRefresh;
            return this;
        }

        public SilentParameters build() {
            return new SilentParameters(this.scopes, this.account, this.authorityUrl, this.forceRefresh);
        }

        @Override
        public String toString() {
            return "SilentParameters.SilentParametersBuilder(scopes=" + this.scopes + ", account=" + this.account + ", authorityUrl=" + this.authorityUrl + ", forceRefresh=" + this.forceRefresh + ")";
        }
    }
}
