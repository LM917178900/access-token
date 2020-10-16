package com.test.lei.tokenUtil;

/**
 * @description: ClientCredentialRequest
 * @author: leiming5
 * @date: 2020-10-09 11:05
 */
public class ClientCredentialRequest extends MsalRequest{
    ClientCredentialRequest(ClientCredentialParameters parameters,
                            ConfidentialClientApplication application,
                            RequestContext requestContext){
        super(application, createMsalGrant(parameters), requestContext );
    }

    private static OAuthAuthorizationGrant createMsalGrant(ClientCredentialParameters parameters){

        return new OAuthAuthorizationGrant(new ClientCredentialsGrant(), parameters.scopes());
    }
}
