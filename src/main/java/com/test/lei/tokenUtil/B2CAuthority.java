package com.test.lei.tokenUtil;

import java.net.URL;

/**
 * @description: B2CAuthority
 * @author: leiming5
 * @date: 2020-10-10 10:31
 */
class B2CAuthority extends Authority {
    static final String B2CTokenEndpointFormat = "https://%s/{tenant}" + TOKEN_ENDPOINT + "?p={policy}";
    String policy;

    B2CAuthority(final URL authorityUrl) {
        super(authorityUrl);
        validateAuthorityUrl();
        setAuthorityProperties();
    }

    private void setAuthorityProperties() {
        String[] segments = canonicalAuthorityUrl.getPath().substring(1).split("/");
        if (segments.length < 3) {
            throw new IllegalArgumentException("B2C \'authority\' Uri should have at least 3 segments in the path (i.e. https://<host>/tfp/<tenant>/<policy>/...)");
        }
        policy = segments[2];
        final String b2cAuthorityFormat = "https://%s/%s/%s/%s/";
        this.authority = String.format(b2cAuthorityFormat, canonicalAuthorityUrl.getAuthority(), segments[0], segments[1], segments[2]);
        this.tokenEndpoint = String.format(B2CTokenEndpointFormat, host);
        this.tokenEndpoint = this.tokenEndpoint.replace("{tenant}", tenant);
        this.tokenEndpoint = this.tokenEndpoint.replace("{policy}", policy);
        this.selfSignedJwtAudience = this.tokenEndpoint;
    }

    @java.lang.SuppressWarnings("all")
    String policy() {
        return this.policy;
    }
}

