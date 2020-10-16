package com.test.lei.tokenUtil;

import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.SAML2BearerGrant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description: SAML11BearerGrant
 * @author: leiming5
 * @date: 2020-10-14 10:15
 */
class SAML11BearerGrant extends SAML2BearerGrant {

    /**
     * The grant type.
     */
    public static com.nimbusds.oauth2.sdk.GrantType grantType = new GrantType(
            "urn:ietf:params:oauth:grant-type:saml1_1-bearer");

    public SAML11BearerGrant(Base64URL assertion) {
        super(assertion);
    }

    @Override
    public Map<String, List<String>> toParameters() {

        Map<String, List<String>> params = super.toParameters();
        params.put("grant_type", Collections.singletonList(grantType.getValue()));
        return params;
    }
}
