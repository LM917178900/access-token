package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

/**
 * @description: RefreshTokenRequest
 * @author: leiming5
 * @date: 2020-10-15 18:40
 */
class RefreshTokenRequest extends MsalRequest {
    RefreshTokenRequest(RefreshTokenParameters parameters, ClientApplicationBase application, RequestContext requestContext) {
        super(application, createAuthenticationGrant(parameters), requestContext);
    }

    private static AbstractMsalAuthorizationGrant createAuthenticationGrant(RefreshTokenParameters parameters) {
        RefreshTokenGrant refreshTokenGrant = new RefreshTokenGrant(new RefreshToken(parameters.refreshToken()));
        return new OAuthAuthorizationGrant(refreshTokenGrant, parameters.scopes());
    }
}
