package com.test.lei.tokenUtil;



import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.PrivateKeyJWT;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.test.lei.tokenUtil.ParameterValidationUtils.validateNotNull;

/**
 * @description: ConfidentialClientApplication
 * @author: leiming5
 * @date: 2020-10-09 10:17
 */
public class ConfidentialClientApplication extends ClientApplicationBase implements IConfidentialClientApplication {
    @Override
    public CompletableFuture<IAuthenticationResult> acquireToken(ClientCredentialParameters parameters) {
        ParameterValidationUtils.validateNotNull("parameters", parameters);
        ClientCredentialRequest clientCredentialRequest = new ClientCredentialRequest(parameters, this, this.createRequestContext(PublicApi.ACQUIRE_TOKEN_FOR_CLIENT));
        return this.executeRequest(clientCredentialRequest);
    }

    public CompletableFuture<IAuthenticationResult> acquireToken(OnBehalfOfParameters parameters) {
        ParameterValidationUtils.validateNotNull("parameters", parameters);
        OnBehalfOfRequest oboRequest = new OnBehalfOfRequest(parameters, this, this.createRequestContext(PublicApi.ACQUIRE_TOKEN_ON_BEHALF_OF));
        return this.executeRequest(oboRequest);
    }

    private ConfidentialClientApplication(ConfidentialClientApplication.Builder builder) {
        super(builder);
        this.log = LoggerFactory.getLogger(ConfidentialClientApplication.class);
        this.initClientAuthentication(builder.clientCredential);
    }

    private void initClientAuthentication(IClientCredential clientCredential) {
        ParameterValidationUtils.validateNotNull("clientCredential", clientCredential);
        if (clientCredential instanceof ClientSecret) {
            this.clientAuthentication = new ClientSecretPost(new ClientID(this.clientId()), new Secret(((ClientSecret)clientCredential).clientSecret()));
        } else if (clientCredential instanceof ClientCertificate) {
            ClientAssertion clientAssertion = JwtHelper.buildJwt(this.clientId(), (ClientCertificate)clientCredential, this.authenticationAuthority.selfSignedJwtAudience());
            this.clientAuthentication = this.createClientAuthFromClientAssertion(clientAssertion);
        } else {
            if (!(clientCredential instanceof ClientAssertion)) {
                throw new IllegalArgumentException("Unsupported client credential");
            }

            this.clientAuthentication = this.createClientAuthFromClientAssertion((ClientAssertion)clientCredential);
        }

    }

    private ClientAuthentication createClientAuthFromClientAssertion(ClientAssertion clientAssertion) {
        try {
            Map<String, List<String>> map = new HashMap();
            map.put("client_assertion_type", Collections.singletonList("urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
            map.put("client_assertion", Collections.singletonList(clientAssertion.assertion()));
            return PrivateKeyJWT.parse(map);
        } catch (ParseException var3) {
            throw new MsalClientException(var3);
        }
    }

    public static ConfidentialClientApplication.Builder builder(String clientId, IClientCredential clientCredential) {
        return new ConfidentialClientApplication.Builder(clientId, clientCredential);
    }

    public static class Builder extends ClientApplicationBase.Builder<ConfidentialClientApplication.Builder> {
        private IClientCredential clientCredential;

        private Builder(String clientId, IClientCredential clientCredential) {
            super(clientId);
            this.clientCredential = clientCredential;
        }

        @Override
        public ConfidentialClientApplication build() {
            return new ConfidentialClientApplication(this);
        }

        @Override
        protected ConfidentialClientApplication.Builder self() {
            return this;
        }
    }
}

