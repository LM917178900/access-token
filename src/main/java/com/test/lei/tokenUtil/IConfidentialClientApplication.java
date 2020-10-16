package com.test.lei.tokenUtil;

import java.util.concurrent.CompletableFuture;

public interface IConfidentialClientApplication extends IClientApplicationBase  {

    CompletableFuture<IAuthenticationResult> acquireToken(ClientCredentialParameters parameters);
}
