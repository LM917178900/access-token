package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.util.URLUtils;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: TokenRequestExecutor
 * @author: leiming5
 * @date: 2020-10-09 17:18
 */
public class TokenRequestExecutor {

    Logger log = LoggerFactory.getLogger(TokenRequestExecutor.class);
    final Authority requestAuthority;
    private final MsalRequest msalRequest;
    private final ServiceBundle serviceBundle;

    TokenRequestExecutor(Authority requestAuthority, MsalRequest msalRequest, ServiceBundle serviceBundle) {
        this.requestAuthority = requestAuthority;
        this.serviceBundle = serviceBundle;
        this.msalRequest = msalRequest;
    }

    AuthenticationResult executeTokenRequest() throws ParseException, MsalServiceException, SerializeException, IOException, com.nimbusds.oauth2.sdk.ParseException {
        OAuthHttpRequest oAuthHttpRequest = this.createOauthHttpRequest();
        HTTPResponse oauthHttpResponse = oAuthHttpRequest.send();
        return this.createAuthenticationResultFromOauthHttpResponse(oauthHttpResponse);
    }

    OAuthHttpRequest createOauthHttpRequest() throws SerializeException, MalformedURLException {
        if (this.requestAuthority.tokenEndpointUrl() == null) {
            throw new SerializeException("The endpoint URI is not specified");
        } else {
            OAuthHttpRequest oauthHttpRequest = new OAuthHttpRequest(HTTPRequest.Method.POST, this.requestAuthority.tokenEndpointUrl(), this.msalRequest.headers().getReadonlyHeaderMap(), this.msalRequest.requestContext(), this.serviceBundle);
            oauthHttpRequest.setContentType(CommonContentTypes.APPLICATION_URLENCODED);
            Map<String, List<String>> params = this.msalRequest.msalAuthorizationGrant().toParameters();
            oauthHttpRequest.setQuery(URLUtils.serializeParameters(params));
            if (this.msalRequest.application().clientAuthentication != null) {
                this.msalRequest.application().clientAuthentication.applyTo(oauthHttpRequest);
            }

            return oauthHttpRequest;
        }
    }

    private AuthenticationResult createAuthenticationResultFromOauthHttpResponse(HTTPResponse oauthHttpResponse) throws com.nimbusds.oauth2.sdk.ParseException {
        if (oauthHttpResponse.getStatusCode() == 200) {
            TokenResponse response = TokenResponse.parseHttpResponse(oauthHttpResponse);
            OIDCTokens tokens = response.getOIDCTokens();
            String refreshToken = null;
            if (tokens.getRefreshToken() != null) {
                refreshToken = tokens.getRefreshToken().getValue();
            }

            AccountCacheEntity accountCacheEntity = null;
            if (tokens.getIDToken() != null) {
                String idTokenJson = tokens.getIDToken().getParsedParts()[1].decodeToString();
                IdToken idToken = (IdToken)JsonHelper.convertJsonToObject(idTokenJson, IdToken.class);
                AuthorityType type = this.msalRequest.application().authenticationAuthority.authorityType;
                if (!StringHelper.isBlank(response.getClientInfo())) {
                    if (type == AuthorityType.B2C) {
                        B2CAuthority authority = (B2CAuthority)this.msalRequest.application().authenticationAuthority;
                        accountCacheEntity = AccountCacheEntity.create(response.getClientInfo(), this.requestAuthority, idToken, authority.policy);
                    } else {
                        accountCacheEntity = AccountCacheEntity.create(response.getClientInfo(), this.requestAuthority, idToken);
                    }
                } else if (type == AuthorityType.ADFS) {
                    accountCacheEntity = AccountCacheEntity.createADFSAccount(this.requestAuthority, idToken);
                }
            }

            long currTimestampSec = (new Date()).getTime() / 1000L;
            AuthenticationResult result = AuthenticationResult.builder().accessToken(tokens.getAccessToken().getValue()).refreshToken(refreshToken).familyId(response.getFoci()).idToken(tokens.getIDTokenString()).environment(this.requestAuthority.host()).expiresOn(currTimestampSec + response.getExpiresIn()).extExpiresOn(response.getExtExpiresIn() > 0L ? currTimestampSec + response.getExtExpiresIn() : 0L).accountCacheEntity(accountCacheEntity).scopes(response.getScope()).build();
            return result;
        } else {
            throw MsalServiceExceptionFactory.fromHttpResponse(oauthHttpResponse);
        }
    }

    Logger getLog() {
        return this.log;
    }

    Authority getRequestAuthority() {
        return this.requestAuthority;
    }

    MsalRequest getMsalRequest() {
        return this.msalRequest;
    }

    ServiceBundle getServiceBundle() {
        return this.serviceBundle;
    }
}
