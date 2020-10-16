package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;

/**
 * @description: AuthorizationCodeRequest
 * @author: leiming5
 * @date: 2020-10-15 18:40
 */
class AuthorizationCodeRequest extends MsalRequest {
    AuthorizationCodeRequest(AuthorizationCodeParameters parameters, ClientApplicationBase application, RequestContext requestContext) {
        super(application, createMsalGrant(parameters), requestContext);
    }

    private static AbstractMsalAuthorizationGrant createMsalGrant(AuthorizationCodeParameters parameters) {
        AuthorizationGrant authorizationGrant = new AuthorizationCodeGrant(new AuthorizationCode(parameters.authorizationCode()), parameters.redirectUri());
        return new OAuthAuthorizationGrant(authorizationGrant, parameters.scopes());
    }
}
