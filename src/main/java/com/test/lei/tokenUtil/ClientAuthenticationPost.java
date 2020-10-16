package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.util.URLUtils;

import javax.mail.internet.ContentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ClientAuthenticationPost
 * @author: leiming5
 * @date: 2020-10-10 09:19
 */
class ClientAuthenticationPost extends ClientAuthentication {
    protected ClientAuthenticationPost(ClientAuthenticationMethod method, ClientID clientID) {
        super(method, clientID);
    }

    Map<String, List<String>> toParameters() {
        Map<String, List<String>> params = new HashMap();
        params.put("client_id", Collections.singletonList(this.getClientID().getValue()));
        return params;
    }

    @Override
    public void applyTo(HTTPRequest httpRequest) throws SerializeException {
        if (httpRequest.getMethod() != HTTPRequest.Method.POST) {
            throw new SerializeException("The HTTP request method must be POST");
        } else {
            ContentType ct = httpRequest.getContentType();
            if (ct == null) {
                throw new SerializeException("Missing HTTP Content-Type header");
            } else if (!ct.match(CommonContentTypes.APPLICATION_URLENCODED)) {
                throw new SerializeException("The HTTP Content-Type header must be " + CommonContentTypes.APPLICATION_URLENCODED);
            } else {
                Map<String, List<String>> params = httpRequest.getQueryParameters();
                params.putAll(this.toParameters());
                String queryString = URLUtils.serializeParameters(params);
                httpRequest.setQuery(queryString);
            }
        }
    }
}