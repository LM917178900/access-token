package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: IdTokenCacheEntity
 * @author: leiming5
 * @date: 2020-10-14 10:55
 */
class IdTokenCacheEntity extends Credential {
    @JsonProperty("credential_type")
    private String credentialType;
    @JsonProperty("realm")
    protected String realm;

    IdTokenCacheEntity() {
    }

    String getKey() {
        List<String> keyParts = new ArrayList();
        keyParts.add(this.homeAccountId);
        keyParts.add(this.environment);
        keyParts.add(this.credentialType);
        keyParts.add(this.clientId);
        keyParts.add(this.realm);
        keyParts.add("");
        return String.join("-", keyParts).toLowerCase();
    }

    public String credentialType() {
        return this.credentialType;
    }

    public String realm() {
        return this.realm;
    }

    public IdTokenCacheEntity credentialType(String credentialType) {
        this.credentialType = credentialType;
        return this;
    }

    public IdTokenCacheEntity realm(String realm) {
        this.realm = realm;
        return this;
    }
}
