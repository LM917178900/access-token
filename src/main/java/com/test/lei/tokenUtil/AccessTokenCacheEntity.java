package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: AccessTokenCacheEntity
 * @author: leiming5
 * @date: 2020-10-09 21:16
 */
public class AccessTokenCacheEntity extends Credential {
    @JsonProperty("credential_type")
    private String credentialType;
    @JsonProperty("realm")
    protected String realm;
    @JsonProperty("target")
    private String target;
    @JsonProperty("cached_at")
    private String cachedAt;
    @JsonProperty("expires_on")
    private String expiresOn;
    @JsonProperty("extended_expires_on")
    private String extExpiresOn;

    AccessTokenCacheEntity() {
    }

    String getKey() {
        List<String> keyParts = new ArrayList();
        keyParts.add(StringHelper.isBlank(this.homeAccountId) ? "" : this.homeAccountId);
        keyParts.add(this.environment);
        keyParts.add(this.credentialType);
        keyParts.add(this.clientId);
        keyParts.add(this.realm);
        keyParts.add(this.target);
        return String.join("-", keyParts).toLowerCase();
    }

    public String credentialType() {
        return this.credentialType;
    }

    public String realm() {
        return this.realm;
    }

    public String target() {
        return this.target;
    }

    public String cachedAt() {
        return this.cachedAt;
    }

    public String expiresOn() {
        return this.expiresOn;
    }

    public String extExpiresOn() {
        return this.extExpiresOn;
    }

    public AccessTokenCacheEntity credentialType(String credentialType) {
        this.credentialType = credentialType;
        return this;
    }

    public AccessTokenCacheEntity realm(String realm) {
        this.realm = realm;
        return this;
    }

    public AccessTokenCacheEntity target(String target) {
        this.target = target;
        return this;
    }

    public AccessTokenCacheEntity cachedAt(String cachedAt) {
        this.cachedAt = cachedAt;
        return this;
    }

    public AccessTokenCacheEntity expiresOn(String expiresOn) {
        this.expiresOn = expiresOn;
        return this;
    }

    public AccessTokenCacheEntity extExpiresOn(String extExpiresOn) {
        this.extExpiresOn = extExpiresOn;
        return this;
    }
}
