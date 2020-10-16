package com.test.lei.tokenUtil;

import java.net.URL;

/**
 * @description: ADFSAuthority
 * @author: leiming5
 * @date: 2020-10-09 13:01
 */
public class ADFSAuthority extends Authority{
    final static String TOKEN_ENDPOINT = "oauth2/token";

    private final static String ADFSAuthorityFormat = "https://%s/%s/";

    ADFSAuthority(final URL authorityUrl) {
        super(authorityUrl);
        this.authority = String.format(ADFSAuthorityFormat, host, tenant);
        this.tokenEndpoint = authority + TOKEN_ENDPOINT;
        this.selfSignedJwtAudience = this.tokenEndpoint;
    }


}
