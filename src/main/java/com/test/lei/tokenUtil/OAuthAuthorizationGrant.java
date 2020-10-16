package com.test.lei.tokenUtil;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: OAuthAuthorizationGrant
 * @author: leiming5
 * @date: 2020-10-09 11:28
 */
class OAuthAuthorizationGrant extends AbstractMsalAuthorizationGrant {
    private AuthorizationGrant grant;
    private final Map<String, List<String>> params;

    private OAuthAuthorizationGrant() {
        this.params = new LinkedHashMap();
        this.params.put("scope", Collections.singletonList("openid profile offline_access"));
    }

    OAuthAuthorizationGrant(AuthorizationGrant grant, Set<String> scopesSet) {
        this(grant, scopesSet != null ? String.join(" ", scopesSet) : null);
    }

    OAuthAuthorizationGrant(AuthorizationGrant grant, String scopes) {
        this();
        this.grant = grant;
        if (!StringHelper.isBlank(scopes)) {
            this.scopes = scopes;
            this.params.put("scope", Collections.singletonList(String.join(" ", (Iterable)this.params.get("scope")) + " " + scopes));
        }

    }

    OAuthAuthorizationGrant(AuthorizationGrant grant, Map<String, List<String>> params) {
        this();
        this.grant = grant;
        if (params != null) {
            this.params.putAll(params);
        }

    }

    public Map<String, List<String>> toParameters() {
        Map<String, List<String>> outParams = new LinkedHashMap();
        outParams.putAll(this.params);
        outParams.put("client_info", Collections.singletonList("1"));
        outParams.putAll(this.grant.toParameters());
        return Collections.unmodifiableMap(outParams);
    }

    AuthorizationGrant getAuthorizationGrant() {
        return this.grant;
    }

    Map<String, List<String>> getCustomParameters() {
        return this.params;
    }
}
