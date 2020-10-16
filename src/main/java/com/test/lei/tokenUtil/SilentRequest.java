package com.test.lei.tokenUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: SilentRequest
 * @author: leiming5
 * @date: 2020-10-15 18:42
 */
class SilentRequest extends MsalRequest {
    private SilentParameters parameters;
    private Authority requestAuthority;

    SilentRequest(SilentParameters parameters, ClientApplicationBase application, RequestContext requestContext) throws MalformedURLException {
        super(application, (AbstractMsalAuthorizationGrant)null, requestContext);
        this.parameters = parameters;
        this.requestAuthority = StringHelper.isBlank(parameters.authorityUrl()) ? application.authenticationAuthority : Authority.createAuthority(new URL(parameters.authorityUrl()));
    }

    public SilentParameters parameters() {
        return this.parameters;
    }

    public Authority requestAuthority() {
        return this.requestAuthority;
    }
}
