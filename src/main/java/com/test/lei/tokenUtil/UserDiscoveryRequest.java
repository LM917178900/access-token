package com.test.lei.tokenUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: UserDiscoveryRequest
 * @author: leiming5
 * @date: 2020-10-14 10:08
 */
class UserDiscoveryRequest {
    private static final Map<String, String> HEADERS = new HashMap();

    UserDiscoveryRequest() {
    }

    static UserDiscoveryResponse execute(String uri, Map<String, String> clientDataHeaders, RequestContext requestContext, ServiceBundle serviceBundle) {
        HashMap<String, String> headers = new HashMap(HEADERS);
        headers.putAll(clientDataHeaders);
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET, uri, headers);
        IHttpResponse response = HttpHelper.executeHttpRequest(httpRequest, requestContext, serviceBundle);
        return (UserDiscoveryResponse)JsonHelper.convertJsonToObject(response.body(), UserDiscoveryResponse.class);
    }

    static {
        HEADERS.put("Accept", "application/json, text/javascript, */*");
    }
}
