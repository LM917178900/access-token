package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: AppMetadataCacheEntity
 * @author: leiming5
 * @date: 2020-10-14 10:55
 */
class AppMetadataCacheEntity {
    public static final String APP_METADATA_CACHE_ENTITY_ID = "appmetadata";
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("environment")
    private String environment;
    @JsonProperty("family_id")
    private String familyId;

    AppMetadataCacheEntity() {
    }

    String getKey() {
        List<String> keyParts = new ArrayList();
        keyParts.add("appmetadata");
        keyParts.add(this.environment);
        keyParts.add(this.clientId);
        return String.join("-", keyParts).toLowerCase();
    }

    public String clientId() {
        return this.clientId;
    }

    public String environment() {
        return this.environment;
    }

    public String familyId() {
        return this.familyId;
    }

    public AppMetadataCacheEntity clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public AppMetadataCacheEntity environment(String environment) {
        this.environment = environment;
        return this;
    }

    public AppMetadataCacheEntity familyId(String familyId) {
        this.familyId = familyId;
        return this;
    }
}
