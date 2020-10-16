package com.test.lei.tokenUtil;

/**
 * @description: AcquireTokenSilentSupplier
 * @author: leiming5
 * @date: 2020-10-15 18:43
 */
class AcquireTokenSilentSupplier extends AuthenticationResultSupplier {
    private SilentRequest silentRequest;

    AcquireTokenSilentSupplier(ClientApplicationBase clientApplication, SilentRequest silentRequest) {
        super(clientApplication, silentRequest);
        this.silentRequest = silentRequest;
    }

    @Override
    AuthenticationResult execute() throws Exception {
        Authority requestAuthority = this.silentRequest.requestAuthority();
        if (requestAuthority.authorityType != AuthorityType.B2C) {
            requestAuthority = this.getAuthorityWithPrefNetworkHost(this.silentRequest.requestAuthority().authority());
        }

        AuthenticationResult res;
        if (this.silentRequest.parameters().account() == null) {
            res = this.clientApplication.tokenCache.getCachedAuthenticationResult(requestAuthority, this.silentRequest.parameters().scopes(), this.clientApplication.clientId());
        } else {
            res = this.clientApplication.tokenCache.getCachedAuthenticationResult(this.silentRequest.parameters().account(), requestAuthority, this.silentRequest.parameters().scopes(), this.clientApplication.clientId());
            if (this.silentRequest.parameters().forceRefresh() || StringHelper.isBlank(res.accessToken())) {
                if (!StringHelper.isBlank(res.refreshToken())) {
                    RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(RefreshTokenParameters.builder(this.silentRequest.parameters().scopes(), res.refreshToken()).build(), this.silentRequest.application(), this.silentRequest.requestContext());
                    AcquireTokenByAuthorizationGrantSupplier acquireTokenByAuthorisationGrantSupplier = new AcquireTokenByAuthorizationGrantSupplier(this.clientApplication, refreshTokenRequest, requestAuthority);
                    res = acquireTokenByAuthorisationGrantSupplier.execute();
                } else {
                    res = null;
                }
            }
        }

        if (res != null && !StringHelper.isBlank(res.accessToken())) {
            return res;
        } else {
            throw new MsalClientException("Token not found it the cache", "cache_miss");
        }
    }
}
