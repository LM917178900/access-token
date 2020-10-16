package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description: ErrorResponse
 * @author: leiming5
 * @date: 2020-10-10 10:34
 */
class ErrorResponse {
    private Integer statusCode;
    private String statusMessage;
    @JsonProperty("error")
    protected String error;
    @JsonProperty("error_description")
    protected String errorDescription;
    @JsonProperty("error_codes")
    protected long[] errorCodes;
    @JsonProperty("suberror")
    protected String subError;
    @JsonProperty("trace_id")
    protected String traceId;
    @JsonProperty("timestamp")
    protected String timestamp;
    @JsonProperty("correlation_id")
    protected String correlation_id;
    @JsonProperty("claims")
    private String claims;

    @java.lang.SuppressWarnings("all")
    public Integer statusCode() {
        return this.statusCode;
    }

    @java.lang.SuppressWarnings("all")
    public String statusMessage() {
        return this.statusMessage;
    }

    @java.lang.SuppressWarnings("all")
    public String error() {
        return this.error;
    }

    @java.lang.SuppressWarnings("all")
    public String errorDescription() {
        return this.errorDescription;
    }

    @java.lang.SuppressWarnings("all")
    public long[] errorCodes() {
        return this.errorCodes;
    }

    @java.lang.SuppressWarnings("all")
    public String subError() {
        return this.subError;
    }

    @java.lang.SuppressWarnings("all")
    public String traceId() {
        return this.traceId;
    }

    @java.lang.SuppressWarnings("all")
    public String timestamp() {
        return this.timestamp;
    }

    @java.lang.SuppressWarnings("all")
    public String correlation_id() {
        return this.correlation_id;
    }

    @java.lang.SuppressWarnings("all")
    public String claims() {
        return this.claims;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse statusCode(final Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse statusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse error(final String error) {
        this.error = error;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse errorDescription(final String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse errorCodes(final long[] errorCodes) {
        this.errorCodes = errorCodes;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse subError(final String subError) {
        this.subError = subError;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse traceId(final String traceId) {
        this.traceId = traceId;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse timestamp(final String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse correlation_id(final String correlation_id) {
        this.correlation_id = correlation_id;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    public ErrorResponse claims(final String claims) {
        this.claims = claims;
        return this;
    }
}
