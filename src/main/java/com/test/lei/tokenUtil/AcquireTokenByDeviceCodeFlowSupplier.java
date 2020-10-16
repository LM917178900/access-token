package com.test.lei.tokenUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description: AcquireTokenByDeviceCodeFlowSupplier
 * @author: leiming5
 * @date: 2020-10-15 18:53
 */
class AcquireTokenByDeviceCodeFlowSupplier extends AuthenticationResultSupplier {
    private DeviceCodeFlowRequest deviceCodeFlowRequest;

    AcquireTokenByDeviceCodeFlowSupplier(PublicClientApplication clientApplication, DeviceCodeFlowRequest deviceCodeFlowRequest) {
        super(clientApplication, deviceCodeFlowRequest);
        this.deviceCodeFlowRequest = deviceCodeFlowRequest;
    }

    @Override
    AuthenticationResult execute() throws Exception {
        Authority requestAuthority = this.clientApplication.authenticationAuthority;
        requestAuthority = this.getAuthorityWithPrefNetworkHost(requestAuthority.authority());
        DeviceCode deviceCode = this.getDeviceCode((AADAuthority)requestAuthority);
        return this.acquireTokenWithDeviceCode(deviceCode, requestAuthority);
    }

    private DeviceCode getDeviceCode(AADAuthority requestAuthority) throws Exception {
        DeviceCode deviceCode = this.deviceCodeFlowRequest.acquireDeviceCode(requestAuthority.deviceCodeEndpoint(), this.clientApplication.clientId(), this.deviceCodeFlowRequest.headers().getReadonlyHeaderMap(), this.clientApplication.getServiceBundle());
        this.deviceCodeFlowRequest.parameters().deviceCodeConsumer().accept(deviceCode);
        return deviceCode;
    }

    private AuthenticationResult acquireTokenWithDeviceCode(DeviceCode deviceCode, Authority requestAuthority) throws Exception {
        this.deviceCodeFlowRequest.createAuthenticationGrant(deviceCode);
        long expirationTimeInSeconds = this.getCurrentSystemTimeInSeconds() + deviceCode.expiresIn();
        AcquireTokenByAuthorizationGrantSupplier acquireTokenByAuthorisationGrantSupplier = new AcquireTokenByAuthorizationGrantSupplier(this.clientApplication, this.deviceCodeFlowRequest, requestAuthority);

        while(this.getCurrentSystemTimeInSeconds() < expirationTimeInSeconds) {
            if (((CompletableFuture)this.deviceCodeFlowRequest.futureReference().get()).isCancelled()) {
                throw new InterruptedException("Acquire token Device Code Flow was interrupted");
            }

            try {
                return acquireTokenByAuthorisationGrantSupplier.execute();
            } catch (MsalServiceException var7) {
                if (!var7.errorCode().equals("authorization_pending")) {
                    throw var7;
                }

                TimeUnit.SECONDS.sleep(deviceCode.interval());
            }
        }

        throw new MsalClientException("Expired Device code", "code_expired");
    }

    private Long getCurrentSystemTimeInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}

