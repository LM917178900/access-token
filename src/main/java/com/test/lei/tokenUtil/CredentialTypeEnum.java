package com.test.lei.tokenUtil;

import java.beans.ConstructorProperties;

public enum CredentialTypeEnum {
    ACCESS_TOKEN("AccessToken"),
    REFRESH_TOKEN("RefreshToken"),
    ID_TOKEN("IdToken");

    private final String value;

    public String value() {
        return this.value;
    }

    @ConstructorProperties({"value"})
    private CredentialTypeEnum(String value) {
        this.value = value;
    }
}
