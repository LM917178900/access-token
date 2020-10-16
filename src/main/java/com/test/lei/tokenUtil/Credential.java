package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description: Credential
 * @author: leiming5
 * @date: 2020-10-09 21:18
 */
class Credential {
    @JsonProperty("home_account_id")
    protected String homeAccountId;
    @JsonProperty("environment")
    protected String environment;
    @JsonProperty("client_id")
    protected String clientId;
    @JsonProperty("secret")
    protected String secret;

    @java.lang.SuppressWarnings("all")
    public String homeAccountId() {
        return this.homeAccountId;
    }

    @java.lang.SuppressWarnings("all")
    public String environment() {
        return this.environment;
    }

    @java.lang.SuppressWarnings("all")
    public String clientId() {
        return this.clientId;
    }

    @java.lang.SuppressWarnings("all")
    public String secret() {
        return this.secret;
    }

    @java.lang.SuppressWarnings("all")
    public Credential homeAccountId(final String homeAccountId) {
        this.homeAccountId = homeAccountId;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public Credential environment(final String environment) {
        this.environment = environment;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public Credential clientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public Credential secret(final String secret) {
        this.secret = secret;
        return this;
    }
}
