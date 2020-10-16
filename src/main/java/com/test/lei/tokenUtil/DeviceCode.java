package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description: DeviceCode
 * @author: leiming5
 * @date: 2020-10-15 18:36
 */
public final class DeviceCode {
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("device_code")
    private String deviceCode;
    @JsonProperty("verification_uri")
    private String verificationUri;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("interval")
    private long interval;
    @JsonProperty("message")
    private String message;
    private transient String correlationId = null;
    private transient String clientId = null;
    private transient String scopes = null;

    public DeviceCode() {
    }

    public String userCode() {
        return this.userCode;
    }

    public String deviceCode() {
        return this.deviceCode;
    }

    public String verificationUri() {
        return this.verificationUri;
    }

    public long expiresIn() {
        return this.expiresIn;
    }

    public long interval() {
        return this.interval;
    }

    public String message() {
        return this.message;
    }

    protected String correlationId() {
        return this.correlationId;
    }

    protected DeviceCode correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    protected String clientId() {
        return this.clientId;
    }

    protected DeviceCode clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    protected String scopes() {
        return this.scopes;
    }

    protected DeviceCode scopes(String scopes) {
        this.scopes = scopes;
        return this;
    }
}
