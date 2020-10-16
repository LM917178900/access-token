package com.test.lei.tokenUtil;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.JWTBearerGrant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: OnBehalfOfRequest
 * @author: leiming5
 * @date: 2020-10-15 19:00
 */
class OnBehalfOfRequest extends MsalRequest {
    OnBehalfOfRequest(OnBehalfOfParameters parameters, ConfidentialClientApplication application, RequestContext requestContext) {
        super(application, createAuthenticationGrant(parameters), requestContext);
    }

    private static OAuthAuthorizationGrant createAuthenticationGrant(OnBehalfOfParameters parameters) {
        JWTBearerGrant jWTBearerGrant;
        try {
            jWTBearerGrant = new JWTBearerGrant(SignedJWT.parse(parameters.userAssertion().getAssertion()));
        } catch (Exception var3) {
            throw new MsalClientException(var3);
        }

        Map<String, List<String>> params = new HashMap();
        params.put("scope", Collections.singletonList(String.join(" ", parameters.scopes())));
        params.put("requested_token_use", Collections.singletonList("on_behalf_of"));
        return new OAuthAuthorizationGrant(jWTBearerGrant, params);
    }
}
