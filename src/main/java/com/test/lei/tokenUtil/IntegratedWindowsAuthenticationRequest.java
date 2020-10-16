package com.test.lei.tokenUtil;

/**
 * @description: IntegratedWindowsAuthenticationRequest
 * @author: leiming5
 * @date: 2020-10-14 10:51
 */
class IntegratedWindowsAuthenticationRequest extends MsalRequest{

    IntegratedWindowsAuthenticationRequest(IntegratedWindowsAuthenticationParameters parameters,
                                           PublicClientApplication application,
                                           RequestContext requestContext){
        super(application, createAuthenticationGrant(parameters), requestContext);
    }

    private static AbstractMsalAuthorizationGrant createAuthenticationGrant
            (IntegratedWindowsAuthenticationParameters parameters){

        return new IntegratedWindowsAuthorizationGrant(parameters.scopes(), parameters.username());
    }
}
