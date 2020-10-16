package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.util.URLUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: DeviceCodeFlowRequest
 * @author: leiming5
 * @date: 2020-10-09 13:12
 */
class DeviceCodeFlowRequest extends MsalRequest {
    private AtomicReference<CompletableFuture<IAuthenticationResult>> futureReference;
    private DeviceCodeFlowParameters parameters;
    private String scopesStr;

    DeviceCodeFlowRequest(DeviceCodeFlowParameters parameters, AtomicReference<CompletableFuture<IAuthenticationResult>> futureReference, PublicClientApplication application, RequestContext requestContext) {
        super(application, (AbstractMsalAuthorizationGrant)null, requestContext);
        this.parameters = parameters;
        this.scopesStr = String.join(" ", parameters.scopes());
        this.futureReference = futureReference;
    }

    DeviceCode acquireDeviceCode(String url, String clientId, Map<String, String> clientDataHeaders, ServiceBundle serviceBundle) throws Exception {
        String urlWithQueryParams = this.createQueryParamsAndAppendToURL(url, clientId);
        Map<String, String> headers = this.appendToHeaders(clientDataHeaders);
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET, urlWithQueryParams, headers);
        IHttpResponse response = HttpHelper.executeHttpRequest(httpRequest, this.requestContext(), serviceBundle);
        return this.parseJsonToDeviceCodeAndSetParameters(response.body(), headers, clientId);
    }

    void createAuthenticationGrant(DeviceCode deviceCode) {
        this.msalAuthorizationGrant = new DeviceCodeAuthorizationGrant(deviceCode, deviceCode.scopes());
    }

    private String createQueryParamsAndAppendToURL(String url, String clientId) {
        Map<String, List<String>> queryParameters = new HashMap();
        queryParameters.put("client_id", Collections.singletonList(clientId));
        String scopesParam = "openid profile offline_access " + this.scopesStr;
        queryParameters.put("scope", Collections.singletonList(scopesParam));
        url = url + "?" + URLUtils.serializeParameters(queryParameters);
        return url;
    }

    private Map<String, String> appendToHeaders(Map<String, String> clientDataHeaders) {
        Map<String, String> headers = new HashMap(clientDataHeaders);
        headers.put("Accept", "application/json");
        return headers;
    }

    private DeviceCode parseJsonToDeviceCodeAndSetParameters(String json, Map<String, String> headers, String clientId) {
        DeviceCode result = (DeviceCode)JsonHelper.convertJsonToObject(json, DeviceCode.class);
        String correlationIdHeader = (String)headers.get("client-request-id");
        if (correlationIdHeader != null) {
            result.correlationId(correlationIdHeader);
        }

        result.clientId(clientId);
        result.scopes(this.scopesStr);
        return result;
    }

    AtomicReference<CompletableFuture<IAuthenticationResult>> futureReference() {
        return this.futureReference;
    }

    DeviceCodeFlowParameters parameters() {
        return this.parameters;
    }

    String scopesStr() {
        return this.scopesStr;
    }
}
