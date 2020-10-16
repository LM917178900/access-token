package com.test.lei.tokenUtil;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @description: HttpHeaders
 * @author: leiming5
 * @date: 2020-10-09 17:09
 */
final class HttpHeaders {
    static final String PRODUCT_HEADER_NAME = "x-client-SKU";
    static final String PRODUCT_HEADER_VALUE = "MSAL.Java";
    static final String PRODUCT_VERSION_HEADER_NAME = "x-client-VER";
    static final String PRODUCT_VERSION_HEADER_VALUE = getProductVersion();
    static final String CPU_HEADER_NAME = "x-client-CPU";
    static final String CPU_HEADER_VALUE = System.getProperty("os.arch");
    static final String OS_HEADER_NAME = "x-client-OS";
    static final String OS_HEADER_VALUE = System.getProperty("os.name");
    static final String APPLICATION_NAME_HEADER_NAME = "x-app-name";
    private final String applicationNameHeaderValue;
    static final String APPLICATION_VERSION_HEADER_NAME = "x-app-ver";
    private final String applicationVersionHeaderValue;
    static final String CORRELATION_ID_HEADER_NAME = "client-request-id";
    private final String correlationIdHeaderValue;
    private static final String REQUEST_CORRELATION_ID_IN_RESPONSE_HEADER_NAME = "return-client-request-id";
    private static final String REQUEST_CORRELATION_ID_IN_RESPONSE_HEADER_VALUE = "true";
    private final String headerValues;
    private final Map<String, String> headerMap = new HashMap();

    HttpHeaders(RequestContext requestContext) {
        this.correlationIdHeaderValue = requestContext.correlationId();
        this.applicationNameHeaderValue = requestContext.applicationName();
        this.applicationVersionHeaderValue = requestContext.applicationVersion();
        this.headerValues = this.initHeaderMap();
    }

    private String initHeaderMap() {
        StringBuilder sb = new StringBuilder();
        BiConsumer<String, String> init = (key, val) -> {
            this.headerMap.put(key, val);
            sb.append(key).append("=").append(val).append(";");
        };
        init.accept("x-client-SKU", "MSAL.Java");
        init.accept("x-client-VER", PRODUCT_VERSION_HEADER_VALUE);
        init.accept("x-client-OS", OS_HEADER_VALUE);
        init.accept("x-client-CPU", CPU_HEADER_VALUE);
        init.accept("return-client-request-id", "true");
        init.accept("client-request-id", this.correlationIdHeaderValue);
        if (!StringUtils.isEmpty(this.applicationNameHeaderValue)) {

            init.accept("x-app-name", this.applicationNameHeaderValue);
        }

        if (!StringUtils.isEmpty(this.applicationVersionHeaderValue)) {
            init.accept("x-app-ver", this.applicationVersionHeaderValue);
        }

        return sb.toString();
    }

    Map<String, String> getReadonlyHeaderMap() {
        return Collections.unmodifiableMap(this.headerMap);
    }

    String getHeaderCorrelationIdValue() {
        return this.correlationIdHeaderValue;
    }

    @Override
    public String toString() {
        return this.headerValues;
    }

    private static String getProductVersion() {
        return HttpHeaders.class.getPackage().getImplementationVersion() == null ? "1.0" : HttpHeaders.class.getPackage().getImplementationVersion();
    }
}
