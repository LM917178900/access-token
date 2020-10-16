package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;

/**
 * @description: MsalServiceException
 * @author: leiming5
 * @date: 2020-10-09 17:32
 */
public class MsalServiceException extends MsalException {
    /**
     * Status code returned from http layer
     */
    private Integer statusCode;
    /**
     * Status message returned from the http layer
     */
    private String statusMessage;
    /**
     * An ID that can be used to piece up a single authentication flow.
     */
    private String correlationId;
    /**
     * Claims included in the claims challenge
     */
    private String claims;
    /**
     * Contains the http headers from the server response that indicated an error.

     * When the server returns a 429 Too Many Requests error, a Retry-After should be set.

     * It is important to read and respect the time specified in the Retry-After header
     */
    private Map<String, List<String>> headers;
    private String subError;

    /**`
     * Initializes a new instance of the exception class with a specified error message

     * @param message the error message that explains the reason for the exception
     */
    public MsalServiceException(final String message, final String error) {
        super(message, error);
    }

    /**
     * Initializes a new instance of the exception class

     * @param errorResponse response object contain information about error returned by server

     * @param httpHeaders http headers from the server response
     */
    public MsalServiceException(final ErrorResponse errorResponse, final Map<String, List<String>> httpHeaders) {
        super(errorResponse.errorDescription, errorResponse.error());
        this.statusCode = errorResponse.statusCode();
        this.statusMessage = errorResponse.statusMessage();
        this.subError = errorResponse.subError();
        this.correlationId = errorResponse.correlation_id();
        this.claims = errorResponse.claims();
        this.headers = httpHeaders;
    }

    /**
     * Initializes a new instance of the exception class

     * @param discoveryResponse response object from instance discovery network call
     */
    public MsalServiceException(final AadInstanceDiscoveryResponse discoveryResponse) {
        super(discoveryResponse.errorDescription(), discoveryResponse.error());
        this.correlationId = discoveryResponse.correlationId();
    }

    /**
     * Status code returned from http layer
     */
    @java.lang.SuppressWarnings("all")
    public Integer statusCode() {
        return this.statusCode;
    }

    /**
     * Status message returned from the http layer
     */
    @java.lang.SuppressWarnings("all")
    public String statusMessage() {
        return this.statusMessage;
    }

    /**
     * An ID that can be used to piece up a single authentication flow.
     */
    @java.lang.SuppressWarnings("all")
    public String correlationId() {
        return this.correlationId;
    }

    /**
     * Claims included in the claims challenge
     */
    @java.lang.SuppressWarnings("all")
    public String claims() {
        return this.claims;
    }

    /**
     * Contains the http headers from the server response that indicated an error.

     * When the server returns a 429 Too Many Requests error, a Retry-After should be set.

     * It is important to read and respect the time specified in the Retry-After header
     */
    @java.lang.SuppressWarnings("all")
    public Map<String, List<String>> headers() {
        return this.headers;
    }

    @java.lang.SuppressWarnings("all")
    String subError() {
        return this.subError;
    }
}
