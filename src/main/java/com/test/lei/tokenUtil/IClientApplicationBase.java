package com.test.lei.tokenUtil;

import com.test.lei.config.AccessConfig;

import javax.net.ssl.SSLSocketFactory;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IClientApplicationBase {
    String DEFAULT_AUTHORITY = AccessConfig.DEFAULT_AUTHORITY;

    String clientId();

    String authority();

    boolean validateAuthority();

    String correlationId();

    boolean logPii();

    Proxy proxy();

    SSLSocketFactory sslSocketFactory();

    ITokenCache tokenCache();

    CompletableFuture<IAuthenticationResult> acquireToken(AuthorizationCodeParameters var1);

    CompletableFuture<IAuthenticationResult> acquireToken(RefreshTokenParameters var1);

    CompletableFuture<IAuthenticationResult> acquireTokenSilently(SilentParameters var1) throws MalformedURLException;

    CompletableFuture<Set<IAccount>> getAccounts();

    CompletableFuture removeAccount(IAccount var1);

}
