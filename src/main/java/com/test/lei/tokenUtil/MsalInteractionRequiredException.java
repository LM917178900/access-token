package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;

/**
 * @description: MsalInteractionRequiredException
 * @author: leiming5
 * @date: 2020-10-10 10:39
 */
public class MsalInteractionRequiredException extends MsalServiceException {
    /**
     * Reason for the MsalInteractionRequiredException, enabling you to do more actions or inform the

     * user depending on your scenario.
     */
    private InteractionRequiredExceptionReason reason;

    /**
     * Initializes a new instance of the exception class

     * @param errorResponse response object contain information about error returned by server

     * @param headerMap http headers from the server response
     */
    public MsalInteractionRequiredException(ErrorResponse errorResponse, Map<String, List<String>> headerMap) {
        super(errorResponse, headerMap);
        reason = InteractionRequiredExceptionReason.fromSubErrorString(errorResponse.subError);
    }

    /**
     * Reason for the MsalInteractionRequiredException, enabling you to do more actions or inform the

     * user depending on your scenario.
     */
    @java.lang.SuppressWarnings("all")
    public InteractionRequiredExceptionReason reason() {
        return this.reason;
    }
}
