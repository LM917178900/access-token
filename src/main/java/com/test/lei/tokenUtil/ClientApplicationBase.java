package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.test.lei.config.AccessConfig;
import org.slf4j.Logger;

import javax.net.ssl.SSLSocketFactory;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * @description: ClientApplicationBase
 * @author: leiming5
 * @date: 2020-10-09 10:23
 */
abstract class ClientApplicationBase implements IClientApplicationBase {

    protected Logger log;
    protected ClientAuthentication clientAuthentication;
    protected Authority authenticationAuthority;
    private ServiceBundle serviceBundle;
    private String clientId;
    private String authority;
    private boolean validateAuthority;
    private String correlationId;
    private boolean logPii;
    private Consumer<List<HashMap<String, String>>> telemetryConsumer;
    private Proxy proxy;
    private SSLSocketFactory sslSocketFactory;
    protected TokenCache tokenCache;
    private String applicationName;
    private String applicationVersion;
    private AadInstanceDiscoveryResponse aadAadInstanceDiscoveryResponse;

    @Override
    public CompletableFuture<IAuthenticationResult> acquireToken(AuthorizationCodeParameters parameters) {
        ParameterValidationUtils.validateNotNull("parameters", parameters);
        AuthorizationCodeRequest authorizationCodeRequest = new AuthorizationCodeRequest(parameters, this, this.createRequestContext(PublicApi.ACQUIRE_TOKEN_BY_AUTHORIZATION_CODE));
        return this.executeRequest(authorizationCodeRequest);
    }

    @Override
    public CompletableFuture<IAuthenticationResult> acquireToken(RefreshTokenParameters parameters) {
        ParameterValidationUtils.validateNotNull("parameters", parameters);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(parameters, this, this.createRequestContext(PublicApi.ACQUIRE_TOKEN_BY_REFRESH_TOKEN));
        return this.executeRequest(refreshTokenRequest);
    }

    CompletableFuture<IAuthenticationResult> executeRequest(MsalRequest msalRequest) {
        AuthenticationResultSupplier supplier = this.getAuthenticationResultSupplier(msalRequest);
        ExecutorService executorService = this.serviceBundle.getExecutorService();
        CompletableFuture<IAuthenticationResult> future = executorService != null ? CompletableFuture.supplyAsync(supplier, executorService) : CompletableFuture.supplyAsync(supplier);
        return future;
    }

    @Override
    public CompletableFuture<IAuthenticationResult> acquireTokenSilently(SilentParameters parameters) throws MalformedURLException {
        ParameterValidationUtils.validateNotNull("parameters", parameters);
        SilentRequest silentRequest = new SilentRequest(parameters, this, this.createRequestContext(PublicApi.ACQUIRE_TOKEN_SILENTLY));
        return this.executeRequest(silentRequest);
    }

    @Override
    public CompletableFuture<Set<IAccount>> getAccounts() {
        MsalRequest msalRequest = new MsalRequest(this, (AbstractMsalAuthorizationGrant)null, this.createRequestContext(PublicApi.GET_ACCOUNTS)) {
        };
        AccountsSupplier supplier = new AccountsSupplier(this, msalRequest);
        CompletableFuture<Set<IAccount>> future = this.serviceBundle.getExecutorService() != null ? CompletableFuture.supplyAsync(supplier, this.serviceBundle.getExecutorService()) : CompletableFuture.supplyAsync(supplier);
        return future;
    }

    @Override
    public CompletableFuture removeAccount(IAccount account) {
        MsalRequest msalRequest = new MsalRequest(this, (AbstractMsalAuthorizationGrant)null, this.createRequestContext(PublicApi.REMOVE_ACCOUNTS)) {
        };
        RemoveAccountRunnable runnable = new RemoveAccountRunnable(msalRequest, account);
        CompletableFuture<Void> future = this.serviceBundle.getExecutorService() != null ? CompletableFuture.runAsync(runnable, this.serviceBundle.getExecutorService()) : CompletableFuture.runAsync(runnable);
        return future;
    }

    AuthenticationResult acquireTokenCommon(MsalRequest msalRequest, Authority requestAuthority) throws Exception {
        HttpHeaders headers = msalRequest.headers();
        if (this.logPii) {
            this.log.debug(LogHelper.createMessage(String.format("Using Client Http Headers: %s", headers), headers.getHeaderCorrelationIdValue()));
        }

        TokenRequestExecutor requestExecutor = new TokenRequestExecutor(requestAuthority, msalRequest, this.serviceBundle);
        AuthenticationResult result = requestExecutor.executeTokenRequest();
        if (this.authenticationAuthority.authorityType.equals(AuthorityType.AAD)) {
            InstanceDiscoveryMetadataEntry instanceDiscoveryMetadata = AadInstanceDiscoveryProvider.getMetadataEntry(requestAuthority.canonicalAuthorityUrl(), this.validateAuthority, msalRequest, this.serviceBundle);
            this.tokenCache.saveTokens(requestExecutor, result, instanceDiscoveryMetadata.preferredCache);
        } else {
            this.tokenCache.saveTokens(requestExecutor, result, this.authenticationAuthority.host);
        }

        return result;
    }

    private AuthenticationResultSupplier getAuthenticationResultSupplier(MsalRequest msalRequest) {
        Object supplier;
        if (msalRequest instanceof DeviceCodeFlowRequest) {
            supplier = new AcquireTokenByDeviceCodeFlowSupplier((PublicClientApplication)this, (DeviceCodeFlowRequest)msalRequest);
        } else if (msalRequest instanceof SilentRequest) {
            supplier = new AcquireTokenSilentSupplier(this, (SilentRequest)msalRequest);
        } else {
            supplier = new AcquireTokenByAuthorizationGrantSupplier(this, msalRequest, (Authority)null);
        }

        return (AuthenticationResultSupplier)supplier;
    }

    RequestContext createRequestContext(PublicApi publicApi) {
        return new RequestContext(this, publicApi);
    }

    ServiceBundle getServiceBundle() {
        return this.serviceBundle;
    }

    protected static String canonicalizeUrl(String authority) {
        authority = authority.toLowerCase();
        if (!authority.endsWith("/")) {
            authority = authority + "/";
        }

        return authority;
    }

    ClientApplicationBase(ClientApplicationBase.Builder<?> builder) {
        this.clientId = builder.clientId;
        this.authority = builder.authority;
        this.validateAuthority = builder.validateAuthority;
        this.correlationId = builder.correlationId;
        this.logPii = builder.logPii;
        this.applicationName = builder.applicationName;
        this.applicationVersion = builder.applicationVersion;
        this.telemetryConsumer = builder.telemetryConsumer;
        this.proxy = builder.proxy;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.serviceBundle = new ServiceBundle(builder.executorService, (IHttpClient)(builder.httpClient == null ? new DefaultHttpClient(builder.proxy, builder.sslSocketFactory) : builder.httpClient), new TelemetryManager(this.telemetryConsumer, builder.onlySendFailureTelemetry));
        this.authenticationAuthority = builder.authenticationAuthority;
        this.tokenCache = new TokenCache(builder.tokenCacheAccessAspect);
        this.aadAadInstanceDiscoveryResponse = builder.aadInstanceDiscoveryResponse;
        if (this.aadAadInstanceDiscoveryResponse != null) {
            AadInstanceDiscoveryProvider.cacheInstanceDiscoveryMetadata(this.authenticationAuthority.host, this.aadAadInstanceDiscoveryResponse);
        }

    }

    @Override
    public String clientId() {
        return this.clientId;
    }

    @Override
    public String authority() {
        return this.authority;
    }

    @Override
    public boolean validateAuthority() {
        return this.validateAuthority;
    }

    @Override
    public String correlationId() {
        return this.correlationId;
    }

    @Override
    public boolean logPii() {
        return this.logPii;
    }

    Consumer<List<HashMap<String, String>>> telemetryConsumer() {
        return this.telemetryConsumer;
    }

    @Override
    public Proxy proxy() {
        return this.proxy;
    }

    @Override
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    @Override
    public TokenCache tokenCache() {
        return this.tokenCache;
    }

    public String applicationName() {
        return this.applicationName;
    }

    public String applicationVersion() {
        return this.applicationVersion;
    }

    public AadInstanceDiscoveryResponse aadAadInstanceDiscoveryResponse() {
        return this.aadAadInstanceDiscoveryResponse;
    }

    abstract static class Builder<T extends ClientApplicationBase.Builder<T>> {
        private String clientId;
        private String authority = AccessConfig.DEFAULT_AUTHORITY;
        private Authority authenticationAuthority = createDefaultAADAuthority();
        private boolean validateAuthority = true;
        private String correlationId = UUID.randomUUID().toString();
        private boolean logPii = false;
        private ExecutorService executorService;
        private Proxy proxy;
        private SSLSocketFactory sslSocketFactory;
        private IHttpClient httpClient;
        private Consumer<List<HashMap<String, String>>> telemetryConsumer;
        private Boolean onlySendFailureTelemetry = false;
        private String applicationName;
        private String applicationVersion;
        private ITokenCacheAccessAspect tokenCacheAccessAspect;
        private AadInstanceDiscoveryResponse aadInstanceDiscoveryResponse;

        public Builder(String clientId) {
            ParameterValidationUtils.validateNotBlank("clientId", clientId);
            this.clientId = clientId;
        }

        abstract T self();

        public T authority(String val) throws MalformedURLException {
            this.authority = ClientApplicationBase.canonicalizeUrl(val);
            switch(Authority.detectAuthorityType(new URL(this.authority))) {
                case AAD:
                    this.authenticationAuthority = new AADAuthority(new URL(this.authority));
                    break;
                case ADFS:
                    this.authenticationAuthority = new ADFSAuthority(new URL(this.authority));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported authority type.");
            }

            return this.self();
        }

        public T b2cAuthority(String val) throws MalformedURLException {
            this.authority = ClientApplicationBase.canonicalizeUrl(val);
            if (Authority.detectAuthorityType(new URL(this.authority)) != AuthorityType.B2C) {
                throw new IllegalArgumentException("Unsupported authority type. Please use B2C authority");
            } else {
                this.authenticationAuthority = new B2CAuthority(new URL(this.authority));
                this.validateAuthority = false;
                return this.self();
            }
        }

        public T validateAuthority(boolean val) {
            this.validateAuthority = val;
            return this.self();
        }

        public T correlationId(String val) {
            ParameterValidationUtils.validateNotBlank("correlationId", val);
            this.correlationId = val;
            return this.self();
        }

        public T logPii(boolean val) {
            this.logPii = val;
            return this.self();
        }

        public T executorService(ExecutorService val) {
            ParameterValidationUtils.validateNotNull("executorService", val);
            this.executorService = val;
            return this.self();
        }

        public T proxy(Proxy val) {
            ParameterValidationUtils.validateNotNull("proxy", val);
            this.proxy = val;
            return this.self();
        }

        public T httpClient(IHttpClient val) {
            ParameterValidationUtils.validateNotNull("httpClient", val);
            this.httpClient = val;
            return this.self();
        }

        public T sslSocketFactory(SSLSocketFactory val) {
            ParameterValidationUtils.validateNotNull("sslSocketFactory", val);
            this.sslSocketFactory = val;
            return this.self();
        }

        T telemetryConsumer(Consumer<List<HashMap<String, String>>> val) {
            ParameterValidationUtils.validateNotNull("telemetryConsumer", val);
            this.telemetryConsumer = val;
            return this.self();
        }

        T onlySendFailureTelemetry(Boolean val) {
            this.onlySendFailureTelemetry = val;
            return this.self();
        }

        public T applicationName(String val) {
            ParameterValidationUtils.validateNotNull("applicationName", val);
            this.applicationName = val;
            return this.self();
        }

        public T applicationVersion(String val) {
            ParameterValidationUtils.validateNotNull("applicationVersion", val);
            this.applicationVersion = val;
            return this.self();
        }

        public T setTokenCacheAccessAspect(ITokenCacheAccessAspect val) {
            ParameterValidationUtils.validateNotNull("tokenCacheAccessAspect", val);
            this.tokenCacheAccessAspect = val;
            return this.self();
        }

        public T aadInstanceDiscoveryResponse(String val) {
            ParameterValidationUtils.validateNotNull("aadInstanceDiscoveryResponse", val);
            this.aadInstanceDiscoveryResponse = AadInstanceDiscoveryProvider.parseInstanceDiscoveryMetadata(val);
            return this.self();
        }

        private static Authority createDefaultAADAuthority() {
            try {
                Authority authority = new AADAuthority(new URL("https://login.microsoftonline.com/common/"));
                return authority;
            } catch (Exception var2) {
                throw new MsalClientException(var2);
            }
        }

        abstract ClientApplicationBase build();
    }

}
