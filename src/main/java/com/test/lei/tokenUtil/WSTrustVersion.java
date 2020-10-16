package com.test.lei.tokenUtil;

/**
 * @description: WSTrustVersion
 * @author: leiming5
 * @date: 2020-10-14 10:12
 */
enum WSTrustVersion {
    WSTRUST13("//s:Envelope/s:Body/wst:RequestSecurityTokenResponseCollection/wst:RequestSecurityTokenResponse/wst:TokenType", "wst:RequestedSecurityToken"),
    WSTRUST2005("//s:Envelope/s:Body/t:RequestSecurityTokenResponse/t:TokenType", "t:RequestedSecurityToken"),
    UNDEFINED("", "");

    private String responseTokenTypePath = "";
    private String responseSecurityTokenPath = "";

    private WSTrustVersion(String tokenType, String responseSecurityToken) {
        this.responseTokenTypePath = tokenType;
        this.responseSecurityTokenPath = responseSecurityToken;
    }

    String responseTokenTypePath() {
        return this.responseTokenTypePath;
    }

    String responseSecurityTokenPath() {
        return this.responseSecurityTokenPath;
    }
}
