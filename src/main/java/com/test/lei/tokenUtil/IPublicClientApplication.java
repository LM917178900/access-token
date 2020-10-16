package com.test.lei.tokenUtil;

import java.util.concurrent.CompletableFuture;

public interface IPublicClientApplication extends IClientApplicationBase {
    CompletableFuture<IAuthenticationResult> acquireToken(UserNamePasswordParameters var1);

    CompletableFuture<IAuthenticationResult> acquireToken(IntegratedWindowsAuthenticationParameters var1);

    CompletableFuture<IAuthenticationResult> acquireToken(DeviceCodeFlowParameters var1);
}
