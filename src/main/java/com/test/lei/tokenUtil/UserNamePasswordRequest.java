package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.auth.Secret;

/**
 * @description: UserNamePasswordRequest
 * @author: leiming5
 * @date: 2020-10-14 10:50
 */
class UserNamePasswordRequest extends MsalRequest{

    UserNamePasswordRequest(UserNamePasswordParameters parameters,
                            PublicClientApplication application,
                            RequestContext requestContext) {
        super(application, createAuthenticationGrant(parameters), requestContext);
    }

    private static OAuthAuthorizationGrant createAuthenticationGrant(
            UserNamePasswordParameters parameters) {

        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant =
                new ResourceOwnerPasswordCredentialsGrant(parameters.username(),
                        new Secret(new String(parameters.password())));

        return new OAuthAuthorizationGrant(resourceOwnerPasswordCredentialsGrant, parameters.scopes());
    }
}
