package com.test.lei.tokenUtil;

import java.net.URL;

/**
 * @description: AADAuthority
 * @author: leiming5
 * @date: 2020-10-09 13:00
 */
public class AADAuthority extends Authority{

    private static final String TENANTLESS_TENANT_NAME = "common";
    private static final String AADAuthorityFormat = "https://%s/%s/";
    private static final String AADtokenEndpointFormat = "https://%s/{tenant}" + TOKEN_ENDPOINT;
    static final String DEVICE_CODE_ENDPOINT = "/oauth2/v2.0/devicecode";
    private static final String deviceCodeEndpointFormat = "https://%s/{tenant}" + DEVICE_CODE_ENDPOINT;
    String deviceCodeEndpoint;

    AADAuthority(final URL authorityUrl) {
        super(authorityUrl);
        validateAuthorityUrl();
        setAuthorityProperties();
        this.authority = String.format(AADAuthorityFormat, host, tenant);
    }

    private void setAuthorityProperties() {
        this.tokenEndpoint = String.format(AADtokenEndpointFormat, host);
        this.tokenEndpoint = this.tokenEndpoint.replace("{tenant}", tenant);
        this.deviceCodeEndpoint = String.format(deviceCodeEndpointFormat, host);
        this.deviceCodeEndpoint = this.deviceCodeEndpoint.replace("{tenant}", tenant);
        this.isTenantless = TENANTLESS_TENANT_NAME.equalsIgnoreCase(tenant);
        this.selfSignedJwtAudience = this.tokenEndpoint;
    }

    @java.lang.SuppressWarnings("all")
    String deviceCodeEndpoint() {
        return this.deviceCodeEndpoint;
    }
}
