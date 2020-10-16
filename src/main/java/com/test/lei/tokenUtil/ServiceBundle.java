package com.test.lei.tokenUtil;

import java.util.concurrent.ExecutorService;

/**
 * @description: ServiceBundle
 * @author: leiming5
 * @date: 2020-10-09 17:14
 */
public class ServiceBundle {
    private ExecutorService executorService;
    private TelemetryManager telemetryManager;
    private IHttpClient httpClient;

    ServiceBundle(ExecutorService executorService
            , IHttpClient httpClient, TelemetryManager telemetryManager
    ) {
        this.executorService = executorService;
        this.telemetryManager = telemetryManager;
        this.httpClient = httpClient;
    }

    ExecutorService getExecutorService() {
        return this.executorService;
    }

    TelemetryManager getTelemetryManager() {
        return this.telemetryManager;
    }

    IHttpClient getHttpClient() {
        return this.httpClient;
    }
}
