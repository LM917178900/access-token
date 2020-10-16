package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: IntegratedWindowsAuthorizationGrant
 * @author: leiming5
 * @date: 2020-10-14 10:04
 */
class IntegratedWindowsAuthorizationGrant extends AbstractMsalAuthorizationGrant {
    private final String userName;

    IntegratedWindowsAuthorizationGrant(Set<String> scopes, String userName) {
        this.userName = userName;
        this.scopes = String.join(" ", scopes);
    }

    @Override
    Map<String, List<String>> toParameters() {
        return null;
    }

    String getUserName() {
        return this.userName;
    }
}
