package com.test.lei.tokenUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @description: HttpHelper
 * @author: leiming5
 * @date: 2020-10-09 17:43
 */
public class HttpHelper {
    private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);

    HttpHelper() {
    }

    static IHttpResponse executeHttpRequest(HttpRequest httpRequest, RequestContext requestContext, ServiceBundle serviceBundle) {
        HttpEvent httpEvent = new HttpEvent();
        TelemetryHelper telemetryHelper = serviceBundle.getTelemetryManager().createTelemetryHelper(requestContext.telemetryRequestId(), requestContext.clientId(), httpEvent, false);
        Throwable var6 = null;

        IHttpResponse httpResponse;
        try {
            addRequestInfoToTelemetry(httpRequest, httpEvent);

            try {
                IHttpClient httpClient = serviceBundle.getHttpClient();
                httpResponse = httpClient.send(httpRequest);
            } catch (Exception var16) {
                httpEvent.setOauthErrorCode("unknown");
                throw new MsalClientException(var16);
            }

            addResponseInfoToTelemetry(httpResponse, httpEvent);
            if (httpResponse.headers() != null) {
                verifyReturnedCorrelationId(httpRequest, httpResponse);
            }
        } catch (Throwable var17) {
            var6 = var17;
            throw var17;
        } finally {
            if (telemetryHelper != null) {
                if (var6 != null) {
                    try {
                        telemetryHelper.close();
                    } catch (Throwable var15) {
                        var6.addSuppressed(var15);
                    }
                } else {
                    telemetryHelper.close();
                }
            }

        }

        return httpResponse;
    }

    private static void addRequestInfoToTelemetry(HttpRequest httpRequest, HttpEvent httpEvent) {
        try {
            httpEvent.setHttpPath(httpRequest.url().toURI());
            httpEvent.setHttpMethod(httpRequest.httpMethod().toString());
            if (!StringHelper.isBlank(httpRequest.url().getQuery())) {
                httpEvent.setQueryParameters(httpRequest.url().getQuery());
            }
        } catch (Exception var4) {
            String correlationId = httpRequest.headerValue("client-request-id");
            log.warn(LogHelper.createMessage("Setting URL telemetry fields failed: " + LogHelper.getPiiScrubbedDetails(var4), correlationId != null ? correlationId : ""));
        }

    }

    private static void addResponseInfoToTelemetry(IHttpResponse httpResponse, HttpEvent httpEvent) {
        httpEvent.setHttpResponseStatus(httpResponse.statusCode());
        Map<String, List<String>> headers = httpResponse.headers();
        String userAgent = HttpUtils.headerValue(headers, "User-Agent");
        if (!StringHelper.isBlank(userAgent)) {
            httpEvent.setUserAgent(userAgent);
        }

        String xMsRequestId = HttpUtils.headerValue(headers, "x-ms-request-id");
        if (!StringHelper.isBlank(xMsRequestId)) {
            httpEvent.setRequestIdHeader(xMsRequestId);
        }

        String xMsClientTelemetry = HttpUtils.headerValue(headers, "x-ms-clitelem");
        if (xMsClientTelemetry != null) {
            XmsClientTelemetryInfo xmsClientTelemetryInfo = XmsClientTelemetryInfo.parseXmsTelemetryInfo(xMsClientTelemetry);
            if (xmsClientTelemetryInfo != null) {
                httpEvent.setXmsClientTelemetryInfo(xmsClientTelemetryInfo);
            }
        }

    }

    private static void verifyReturnedCorrelationId(HttpRequest httpRequest, IHttpResponse httpResponse) {
        String sentCorrelationId = httpRequest.headerValue("client-request-id");
        String returnedCorrelationId = HttpUtils.headerValue(httpResponse.headers(), "client-request-id");
        if (StringHelper.isBlank(returnedCorrelationId) || !returnedCorrelationId.equals(sentCorrelationId)) {
            String msg = LogHelper.createMessage(String.format("Sent (%s) Correlation Id is not same as received (%s).", sentCorrelationId, returnedCorrelationId), sentCorrelationId);
            log.info(msg);
        }

    }
}
