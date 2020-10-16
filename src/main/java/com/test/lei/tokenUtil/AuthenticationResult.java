package com.test.lei.tokenUtil;

import com.nimbusds.jwt.JWTParser;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: AuthenticationResult
 * @author: leiming5
 * @date: 2020-10-09 13:09
 */
final class AuthenticationResult implements Serializable, IAuthenticationResult {
    private static final long serialVersionUID = 1L;
    private final String accessToken;
    private final long expiresOn;
    private final long extExpiresOn;
    private final String refreshToken;
    private final String familyId;
    private final String idToken;
    private final AtomicReference<Object> idTokenObject = new AtomicReference();
    private final AccountCacheEntity accountCacheEntity;
    private final AtomicReference<Object> account = new AtomicReference();
    private String environment;
    private final AtomicReference<Object> expiresOnDate = new AtomicReference();
    private final String scopes;

    private IdToken getIdTokenObj() {
        if (StringHelper.isBlank(this.idToken)) {
            return null;
        } else {
            try {
                String idTokenJson = JWTParser.parse(this.idToken).getParsedParts()[1].decodeToString();
                return (IdToken)JsonHelper.convertJsonToObject(idTokenJson, IdToken.class);
            } catch (ParseException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    private IAccount getAccount() {
        return this.accountCacheEntity == null ? null : this.accountCacheEntity.toAccount();
    }

    @ConstructorProperties({"accessToken", "expiresOn", "extExpiresOn", "refreshToken", "familyId", "idToken", "accountCacheEntity", "environment", "scopes"})
    AuthenticationResult(String accessToken, long expiresOn, long extExpiresOn, String refreshToken, String familyId, String idToken, AccountCacheEntity accountCacheEntity, String environment, String scopes) {
        this.accessToken = accessToken;
        this.expiresOn = expiresOn;
        this.extExpiresOn = extExpiresOn;
        this.refreshToken = refreshToken;
        this.familyId = familyId;
        this.idToken = idToken;
        this.accountCacheEntity = accountCacheEntity;
        this.environment = environment;
        this.scopes = scopes;
    }

    public static AuthenticationResult.AuthenticationResultBuilder builder() {
        return new AuthenticationResult.AuthenticationResultBuilder();
    }

    @Override
    public String accessToken() {
        return this.accessToken;
    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public String idToken() {
        return this.idToken;
    }

    public String environment() {
        return this.environment;
    }

    public String scopes() {
        return this.scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AuthenticationResult)) {
            return false;
        } else {
            AuthenticationResult other;
            label136: {
                other = (AuthenticationResult)o;
                Object this$accessToken = this.accessToken();
                Object other$accessToken = other.accessToken();
                if (this$accessToken == null) {
                    if (other$accessToken == null) {
                        break label136;
                    }
                } else if (this$accessToken.equals(other$accessToken)) {
                    break label136;
                }

                return false;
            }

            if (this.expiresOn() != other.expiresOn()) {
                return false;
            } else if (this.extExpiresOn() != other.extExpiresOn()) {
                return false;
            } else {
                Object this$refreshToken = this.refreshToken();
                Object other$refreshToken = other.refreshToken();
                if (this$refreshToken == null) {
                    if (other$refreshToken != null) {
                        return false;
                    }
                } else if (!this$refreshToken.equals(other$refreshToken)) {
                    return false;
                }

                label119: {
                    Object this$familyId = this.familyId();
                    Object other$familyId = other.familyId();
                    if (this$familyId == null) {
                        if (other$familyId == null) {
                            break label119;
                        }
                    } else if (this$familyId.equals(other$familyId)) {
                        break label119;
                    }

                    return false;
                }

                label112: {
                    Object this$idToken = this.idToken();
                    Object other$idToken = other.idToken();
                    if (this$idToken == null) {
                        if (other$idToken == null) {
                            break label112;
                        }
                    } else if (this$idToken.equals(other$idToken)) {
                        break label112;
                    }

                    return false;
                }

                Object this$idTokenObject = this.idTokenObject();
                Object other$idTokenObject = other.idTokenObject();
                if (this$idTokenObject == null) {
                    if (other$idTokenObject != null) {
                        return false;
                    }
                } else if (!this$idTokenObject.equals(other$idTokenObject)) {
                    return false;
                }

                Object this$accountCacheEntity = this.accountCacheEntity();
                Object other$accountCacheEntity = other.accountCacheEntity();
                if (this$accountCacheEntity == null) {
                    if (other$accountCacheEntity != null) {
                        return false;
                    }
                } else if (!this$accountCacheEntity.equals(other$accountCacheEntity)) {
                    return false;
                }

                label91: {
                    Object this$account = this.account();
                    Object other$account = other.account();
                    if (this$account == null) {
                        if (other$account == null) {
                            break label91;
                        }
                    } else if (this$account.equals(other$account)) {
                        break label91;
                    }

                    return false;
                }

                Object this$environment = this.environment();
                Object other$environment = other.environment();
                if (this$environment == null) {
                    if (other$environment != null) {
                        return false;
                    }
                } else if (!this$environment.equals(other$environment)) {
                    return false;
                }

                Object this$expiresOnDate = this.expiresOnDate();
                Object other$expiresOnDate = other.expiresOnDate();
                if (this$expiresOnDate == null) {
                    if (other$expiresOnDate != null) {
                        return false;
                    }
                } else if (!this$expiresOnDate.equals(other$expiresOnDate)) {
                    return false;
                }

                Object this$scopes = this.scopes();
                Object other$scopes = other.scopes();
                if (this$scopes == null) {
                    if (other$scopes != null) {
                        return false;
                    }
                } else if (!this$scopes.equals(other$scopes)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Override
    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $accessToken = this.accessToken();
         result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
        long $expiresOn = this.expiresOn();
        result = result * 59 + (int)($expiresOn >>> 32 ^ $expiresOn);
        long $extExpiresOn = this.extExpiresOn();
        result = result * 59 + (int)($extExpiresOn >>> 32 ^ $extExpiresOn);
        Object $refreshToken = this.refreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        Object $familyId = this.familyId();
        result = result * 59 + ($familyId == null ? 43 : $familyId.hashCode());
        Object $idToken = this.idToken();
        result = result * 59 + ($idToken == null ? 43 : $idToken.hashCode());
        Object $idTokenObject = this.idTokenObject();
        result = result * 59 + ($idTokenObject == null ? 43 : $idTokenObject.hashCode());
        Object $accountCacheEntity = this.accountCacheEntity();
        result = result * 59 + ($accountCacheEntity == null ? 43 : $accountCacheEntity.hashCode());
        Object $account = this.account();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        Object $environment = this.environment();
        result = result * 59 + ($environment == null ? 43 : $environment.hashCode());
        Object $expiresOnDate = this.expiresOnDate();
        result = result * 59 + ($expiresOnDate == null ? 43 : $expiresOnDate.hashCode());
        Object $scopes = this.scopes();
        result = result * 59 + ($scopes == null ? 43 : $scopes.hashCode());
        return result;
    }

    long expiresOn() {
        return this.expiresOn;
    }

    long extExpiresOn() {
        return this.extExpiresOn;
    }

    String familyId() {
        return this.familyId;
    }

    IdToken idTokenObject() {
        Object value = this.idTokenObject.get();
        if (value == null) {
            synchronized(this.idTokenObject) {
                value = this.idTokenObject.get();
                if (value == null) {
                    IdToken actualValue = this.getIdTokenObj();
                    value = actualValue == null ? this.idTokenObject : actualValue;
                    this.idTokenObject.set(value);
                }
            }
        }

        return (IdToken)((IdToken)(value == this.idTokenObject ? null : value));
    }

    AccountCacheEntity accountCacheEntity() {
        return this.accountCacheEntity;
    }

    public IAccount account() {
        Object value = this.account.get();
        if (value == null) {
            synchronized(this.account) {
                value = this.account.get();
                if (value == null) {
                    IAccount actualValue = this.getAccount();
                    value = actualValue == null ? this.account : actualValue;
                    this.account.set(value);
                }
            }
        }

        return (IAccount)((IAccount)(value == this.account ? null : value));
    }

    public Date expiresOnDate() {
        Object value = this.expiresOnDate.get();
        if (value == null) {
            synchronized(this.expiresOnDate) {
                value = this.expiresOnDate.get();
                if (value == null) {
                    Date actualValue = new Date(this.expiresOn * 1000L);
                    value = actualValue == null ? this.expiresOnDate : actualValue;
                    this.expiresOnDate.set(value);
                }
            }
        }

        return (Date)((Date)(value == this.expiresOnDate ? null : value));
    }

    public static class AuthenticationResultBuilder {
        private String accessToken;
        private long expiresOn;
        private long extExpiresOn;
        private String refreshToken;
        private String familyId;
        private String idToken;
        private AccountCacheEntity accountCacheEntity;
        private String environment;
        private String scopes;

        AuthenticationResultBuilder() {
        }

        public AuthenticationResult.AuthenticationResultBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder expiresOn(long expiresOn) {
            this.expiresOn = expiresOn;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder extExpiresOn(long extExpiresOn) {
            this.extExpiresOn = extExpiresOn;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder familyId(String familyId) {
            this.familyId = familyId;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder idToken(String idToken) {
            this.idToken = idToken;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder accountCacheEntity(AccountCacheEntity accountCacheEntity) {
            this.accountCacheEntity = accountCacheEntity;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public AuthenticationResult.AuthenticationResultBuilder scopes(String scopes) {
            this.scopes = scopes;
            return this;
        }

        public AuthenticationResult build() {
            return new AuthenticationResult(this.accessToken, this.expiresOn, this.extExpiresOn, this.refreshToken, this.familyId, this.idToken, this.accountCacheEntity, this.environment, this.scopes);
        }

        @Override
        public String toString() {
            return "AuthenticationResult.AuthenticationResultBuilder(accessToken=" + this.accessToken + ", expiresOn=" + this.expiresOn + ", extExpiresOn=" + this.extExpiresOn + ", refreshToken=" + this.refreshToken + ", familyId=" + this.familyId + ", idToken=" + this.idToken + ", accountCacheEntity=" + this.accountCacheEntity + ", environment=" + this.environment + ", scopes=" + this.scopes + ")";
        }
    }
}
