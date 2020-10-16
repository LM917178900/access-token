package com.test.lei.tokenUtil;


import java.beans.ConstructorProperties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: MsalRequest
 * @author: leiming5
 * @date: 2020-10-09 11:28
 */
abstract class MsalRequest {

    private final ClientApplicationBase application;
    AbstractMsalAuthorizationGrant msalAuthorizationGrant;
    private final RequestContext requestContext;
    private final AtomicReference<Object> headers = new AtomicReference();

    ClientApplicationBase application() {
        return this.application;
    }

    AbstractMsalAuthorizationGrant msalAuthorizationGrant() {
        return this.msalAuthorizationGrant;
    }

    RequestContext requestContext() {
        return this.requestContext;
    }

    @ConstructorProperties({"application", "msalAuthorizationGrant", "requestContext"})
    public MsalRequest(ClientApplicationBase application, AbstractMsalAuthorizationGrant msalAuthorizationGrant, RequestContext requestContext) {
        this.application = application;
        this.msalAuthorizationGrant = msalAuthorizationGrant;
        this.requestContext = requestContext;
    }

    HttpHeaders headers() {
        Object value = this.headers.get();
        if (value == null) {
            synchronized(this.headers) {
                value = this.headers.get();
                if (value == null) {
                    HttpHeaders actualValue = new HttpHeaders(this.requestContext);
                    value = actualValue == null ? this.headers : actualValue;
                    this.headers.set(value);
                }
            }
        }

        return (HttpHeaders)((HttpHeaders)(value == this.headers ? null : value));
    }

}
