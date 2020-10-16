package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;

/**
 * @description: AbstractMsalAuthorizationGrant
 * @author: leiming5
 * @date: 2020-10-09 11:40
 */
abstract class AbstractMsalAuthorizationGrant {

    abstract Map<String, List<String>> toParameters();

    static final String SCOPE_PARAM_NAME = "scope";
    static final String SCOPES_DELIMITER = " ";

    static final String SCOPE_OPEN_ID = "openid";
    static final String SCOPE_PROFILE = "profile";
    static final String SCOPE_OFFLINE_ACCESS = "offline_access";

    static final String COMMON_SCOPES_PARAM = SCOPE_OPEN_ID + SCOPES_DELIMITER +
            SCOPE_PROFILE + SCOPES_DELIMITER +
            SCOPE_OFFLINE_ACCESS;

    String scopes;

    String getScopes() {
        return scopes;
    }
}
