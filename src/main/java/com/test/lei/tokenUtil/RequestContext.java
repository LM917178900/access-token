package com.test.lei.tokenUtil;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @description: RequestContext
 * @author: leiming5
 * @date: 2020-10-09 11:08
 */
public class RequestContext {

    private String telemetryRequestId;
    private String clientId;
    private String correlationId;
    private PublicApi publicApi;
    private String applicationName;
    private String applicationVersion;

    public RequestContext(ClientApplicationBase clientApplication, PublicApi publicApi) {
        this.clientId = StringUtils.isEmpty(clientApplication.clientId()) ? "unset_client_id" : clientApplication.clientId();
        this.correlationId = StringUtils.isEmpty(clientApplication.correlationId()) ? generateNewCorrelationId() : clientApplication.correlationId();
        this.applicationVersion = clientApplication.applicationVersion();
        this.applicationName = clientApplication.applicationName();
        this.publicApi = publicApi;
    }

    private static String generateNewCorrelationId() {
        return UUID.randomUUID().toString();
    }

    String telemetryRequestId() {
        return this.telemetryRequestId;
    }

    String clientId() {
        return this.clientId;
    }

    String correlationId() {
        return this.correlationId;
    }

    PublicApi publicApi() {
        return this.publicApi;
    }

    String applicationName() {
        return this.applicationName;
    }

    String applicationVersion() {
        return this.applicationVersion;
    }

    RequestContext telemetryRequestId(String telemetryRequestId) {
        this.telemetryRequestId = telemetryRequestId;
        return this;
    }
}
