package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;

/**
 * @description: IdToken
 * @author: leiming5
 * @date: 2020-10-09 20:46
 */
public class IdToken {
    static final String ISSUER = "iss";
    static final String SUBJECT = "sub";
    static final String AUDIENCE = "aud";
    static final String EXPIRATION_TIME = "exp";
    static final String ISSUED_AT = "issuedAt";
    static final String NOT_BEFORE = "nbf";
    static final String NAME = "name";
    static final String PREFERRED_USERNAME = "preferred_username";
    static final String OBJECT_IDENTIFIER = "oid";
    static final String TENANT_IDENTIFIER = "tid";
    static final String UPN = "upn";
    static final String UNIQUE_NAME = "unique_name";
    @JsonProperty("iss")
    protected String issuer;
    @JsonProperty("sub")
    protected String subject;
    @JsonProperty("aud")
    protected String audience;
    @JsonProperty("exp")
    protected Long expirationTime;
    @JsonProperty("iat")
    protected Long issuedAt;
    @JsonProperty("nbf")
    protected Long notBefore;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("preferred_username")
    protected String preferredUsername;
    @JsonProperty("oid")
    protected String objectIdentifier;
    @JsonProperty("tid")
    protected String tenantIdentifier;
    @JsonProperty("upn")
    protected String upn;
    @JsonProperty("unique_name")
    protected String uniqueName;

    IdToken() {
    }

    static IdToken createFromJWTClaims(JWTClaimsSet claims) throws ParseException {
        IdToken idToken = new IdToken();
        idToken.issuer = claims.getStringClaim("iss");
        idToken.subject = claims.getStringClaim("sub");
        idToken.audience = claims.getStringClaim("aud");
        idToken.expirationTime = claims.getLongClaim("exp");
        idToken.issuedAt = claims.getLongClaim("issuedAt");
        idToken.notBefore = claims.getLongClaim("nbf");
        idToken.name = claims.getStringClaim("name");
        idToken.preferredUsername = claims.getStringClaim("preferred_username");
        idToken.objectIdentifier = claims.getStringClaim("oid");
        idToken.tenantIdentifier = claims.getStringClaim("tid");
        idToken.upn = claims.getStringClaim("upn");
        idToken.uniqueName = claims.getStringClaim("unique_name");
        return idToken;
    }
}
