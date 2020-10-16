package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: RefreshTokenCacheEntity
 * @author: leiming5
 * @date: 2020-10-14 10:54
 */
class RefreshTokenCacheEntity extends Credential {
    @JsonProperty("credential_type")
    private String credentialType;
    @JsonProperty("family_id")
    private String family_id;

    RefreshTokenCacheEntity() {
    }

    boolean isFamilyRT() {
        return !StringHelper.isBlank(this.family_id);
    }

    String getKey() {
        List<String> keyParts = new ArrayList();
        keyParts.add(this.homeAccountId);
        keyParts.add(this.environment);
        keyParts.add(this.credentialType);
        if (this.isFamilyRT()) {
            keyParts.add(this.family_id);
        } else {
            keyParts.add(this.clientId);
        }

        keyParts.add("");
        keyParts.add("");
        return String.join("-", keyParts).toLowerCase();
    }

    public String credentialType() {
        return this.credentialType;
    }

    public String family_id() {
        return this.family_id;
    }

    public RefreshTokenCacheEntity credentialType(String credentialType) {
        this.credentialType = credentialType;
        return this;
    }

    public RefreshTokenCacheEntity family_id(String family_id) {
        this.family_id = family_id;
        return this;
    }
}
