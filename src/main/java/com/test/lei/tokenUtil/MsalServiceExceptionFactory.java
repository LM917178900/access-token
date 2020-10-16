package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: MsalServiceExceptionFactory
 * @author: leiming5
 * @date: 2020-10-10 10:32
 */
class MsalServiceExceptionFactory {

    private MsalServiceExceptionFactory(){
    }

    static MsalServiceException fromHttpResponse(HTTPResponse httpResponse) {
        String responseContent = httpResponse.getContent();
        if (responseContent != null && !StringHelper.isBlank(responseContent)) {
            ErrorResponse errorResponse = (ErrorResponse)JsonHelper.convertJsonToObject(responseContent, ErrorResponse.class);
            errorResponse.statusCode(httpResponse.getStatusCode());
            errorResponse.statusMessage(httpResponse.getStatusMessage());
            return (MsalServiceException)(errorResponse.error() != null && errorResponse.error().equalsIgnoreCase("invalid_grant") && isInteractionRequired(errorResponse.subError) ? new MsalInteractionRequiredException(errorResponse, httpResponse.getHeaderMap()) : new MsalServiceException(errorResponse, httpResponse.getHeaderMap()));
        } else {
            return new MsalServiceException("Unknown Service Exception", "unknown");
        }
    }

    private static boolean isInteractionRequired(String subError){

        String[] nonUiSubErrors = {"client_mismatch", "protection_policy_required"};
        Set<String> set = new HashSet<>(Arrays.asList(nonUiSubErrors));

        if(StringHelper.isBlank(subError)){
            return true;
        }

        return !set.contains(subError);
    }
}

