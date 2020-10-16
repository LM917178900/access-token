package com.test.lei.tokenUtil;

import java.util.Set;
import java.util.concurrent.CompletionException;

/**
 * @description: RemoveAccountRunnable
 * @author: leiming5
 * @date: 2020-10-15 19:03
 */
class RemoveAccountRunnable implements Runnable {
    private RequestContext requestContext;
    private ClientApplicationBase clientApplication;
    IAccount account;

    RemoveAccountRunnable(MsalRequest msalRequest, IAccount account) {
        this.clientApplication = msalRequest.application();
        this.requestContext = msalRequest.requestContext();
        this.account = account;
    }

    public void run() {
        try {
            Set<String> aliases = AadInstanceDiscoveryProvider.getAliases(this.clientApplication.authenticationAuthority.host());
            this.clientApplication.tokenCache.removeAccount(this.clientApplication.clientId(), this.account, aliases);
        } catch (Exception var2) {
            this.clientApplication.log.error(LogHelper.createMessage("Execution of " + this.getClass() + " failed.", this.requestContext.correlationId()), var2);
            throw new CompletionException(var2);
        }
    }
}
