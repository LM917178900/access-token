package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.MultivaluedMapUtils;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ClientCredentialsGrant
 * @author: leiming5
 * @date: 2020-10-09 11:53
 */
@Immutable
public class ClientCredentialsGrant extends AuthorizationGrant {

    public static final GrantType GRANT_TYPE;

    public ClientCredentialsGrant() {
        super(GRANT_TYPE);
    }

    @Override
    public Map<String, List<String>> toParameters() {
        Map<String, List<String>> params = new LinkedHashMap();
        params.put("grant_type", Collections.singletonList(GRANT_TYPE.getValue()));
        return params;
    }

    public static ClientCredentialsGrant parse(Map<String, List<String>> params) throws ParseException {
        String grantTypeString = (String) MultivaluedMapUtils.getFirstValue(params, "grant_type");
        String msg;
        if (grantTypeString == null) {
            msg = "Missing \"grant_type\" parameter";
            throw new ParseException(msg, OAuth2Error.INVALID_REQUEST.appendDescription(": " + msg));
        } else if (!GrantType.parse(grantTypeString).equals(GRANT_TYPE)) {
            msg = "The \"grant_type\" must be " + GRANT_TYPE;
            throw new ParseException(msg, OAuth2Error.UNSUPPORTED_GRANT_TYPE.appendDescription(": " + msg));
        } else {
            return new ClientCredentialsGrant();
        }
    }

    static {
        GRANT_TYPE = GrantType.CLIENT_CREDENTIALS;
    }
}
