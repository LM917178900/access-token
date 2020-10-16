package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.test.lei.config.Config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description: OAuthHttpRequest
 * @author: leiming5
 * @date: 2020-10-10 09:31
 */
class OAuthHttpRequest extends HTTPRequest {
    private final Map<String, String> extraHeaderParams;
    private final ServiceBundle serviceBundle;
    private final RequestContext requestContext;

    OAuthHttpRequest(Method method, URL url, Map<String, String> extraHeaderParams, RequestContext requestContext, ServiceBundle serviceBundle) {
        super(method, url);
        this.extraHeaderParams = extraHeaderParams;
        this.requestContext = requestContext;
        this.serviceBundle = serviceBundle;
    }

    @Override
    public HTTPResponse send() throws IOException {
        Map<String, String> httpHeaders = this.configureHttpHeaders();
        StringBuilder bf = new StringBuilder(this.getQuery());

        if (Config.authenticationType.equalsIgnoreCase("ServicePrincipal")) {
            bf.append("&client_id=").append(Config.clientId);
        }
        bf.append("&client_secret=").append(Config.appSecret);

        this.setQuery(bf.toString());
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, this.getURL().toString(), httpHeaders, this.getQuery());
        IHttpResponse httpResponse = HttpHelper.executeHttpRequest(httpRequest, this.requestContext, this.serviceBundle);
        return this.createOauthHttpResponseFromHttpResponse(httpResponse);
    }

    private Map<String, String> configureHttpHeaders() {
        Map<String, String> httpHeaders = new HashMap(this.extraHeaderParams);
        httpHeaders.put("Content-Type", CommonContentTypes.APPLICATION_URLENCODED.toString());
        if (this.getAuthorization() != null) {
            httpHeaders.put("Authorization", this.getAuthorization());
        }

        return httpHeaders;
    }

    private HTTPResponse createOauthHttpResponseFromHttpResponse(IHttpResponse httpResponse) throws IOException {
        HTTPResponse response = new HTTPResponse(httpResponse.statusCode());
        String location = HttpUtils.headerValue(httpResponse.headers(), "Location");
        if (!StringHelper.isBlank(location)) {
            try {
                response.setLocation(new URI(location));
            } catch (URISyntaxException var8) {
                throw new IOException("Invalid location URI " + location, var8);
            }
        }

        try {
            String contentType = HttpUtils.headerValue(httpResponse.headers(), "Content-Type");
            if (!StringHelper.isBlank(contentType)) {
                response.setContentType(contentType);
            }
        } catch (ParseException var9) {
            throw new IOException("Couldn't parse Content-Type header: " + var9.getMessage(), var9);
        }

        Map<String, List<String>> headers = httpResponse.headers();
        Iterator var5 = headers.entrySet().iterator();

        while (true) {
            Map.Entry header;
            String headerValue;
            do {
                do {
                    if (!var5.hasNext()) {
                        if (!StringHelper.isBlank(httpResponse.body())) {
                            response.setContent(httpResponse.body());
                        }

                        return response;
                    }

                    header = (Map.Entry) var5.next();
                } while (StringHelper.isBlank((String) header.getKey()));

                headerValue = response.getHeaderValue((String) header.getKey());
            } while (headerValue != null && !StringHelper.isBlank(headerValue));

            response.setHeader((String) header.getKey(), (String[]) ((List) header.getValue()).toArray(new String[0]));
        }
    }

    Map<String, String> getExtraHeaderParams() {
        return this.extraHeaderParams;
    }
}
