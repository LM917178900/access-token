package com.test.lei.tokenUtil;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

/**
 * @description: AccountsSupplier
 * @author: leiming5
 * @date: 2020-10-15 19:02
 */
class AccountsSupplier implements Supplier<Set<IAccount>> {
    ClientApplicationBase clientApplication;
    MsalRequest msalRequest;

    AccountsSupplier(ClientApplicationBase clientApplication, MsalRequest msalRequest) {
        this.clientApplication = clientApplication;
        this.msalRequest = msalRequest;
    }

    public Set<IAccount> get() {
        try {
            InstanceDiscoveryMetadataEntry instanceDiscoveryData = AadInstanceDiscoveryProvider.getMetadataEntry(new URL(this.clientApplication.authority()), this.clientApplication.validateAuthority(), this.msalRequest, this.clientApplication.getServiceBundle());
            return this.clientApplication.tokenCache.getAccounts(this.clientApplication.clientId(), instanceDiscoveryData.aliases);
        } catch (Exception var2) {
            this.clientApplication.log.error(LogHelper.createMessage("Execution of " + this.getClass() + " failed.", this.msalRequest.headers().getHeaderCorrelationIdValue()), var2);
            throw new CompletionException(var2);
        }
    }
}
