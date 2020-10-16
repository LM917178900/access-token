package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: AccountCacheEntity
 * @author: leiming5
 * @date: 2020-10-09 21:38
 */
public class AccountCacheEntity implements Serializable {
    static final String MSSTS_ACCOUNT_TYPE = "MSSTS";
    static final String ADFS_ACCOUNT_TYPE = "ADFS";
    @JsonProperty("home_account_id")
    protected String homeAccountId;
    @JsonProperty("environment")
    protected String environment;
    @JsonProperty("realm")
    protected String realm;
    @JsonProperty("local_account_id")
    protected String localAccountId;
    @JsonProperty("username")
    protected String username;
    @JsonProperty("name")
    protected String name;
    @JsonProperty("client_info")
    protected String clientInfoStr;
    @JsonProperty("authority_type")
    protected String authorityType;

    AccountCacheEntity() {
    }

    ClientInfo clientInfo() {
        return ClientInfo.createFromJson(this.clientInfoStr);
    }

    String getKey() {
        List<String> keyParts = new ArrayList();
        keyParts.add(this.homeAccountId);
        keyParts.add(this.environment);
        keyParts.add(StringHelper.isBlank(this.realm) ? "" : this.realm);
        return String.join("-", keyParts).toLowerCase();
    }

    static AccountCacheEntity create(String clientInfoStr, Authority requestAuthority, IdToken idToken, String policy) {
        AccountCacheEntity account = new AccountCacheEntity();
        account.authorityType("MSSTS");
        account.clientInfoStr = clientInfoStr;
        account.homeAccountId(policy != null ? account.clientInfo().toAccountIdentifier() + "-" + policy : account.clientInfo().toAccountIdentifier());
        account.environment(requestAuthority.host());
        account.realm(requestAuthority.tenant());
        if (idToken != null) {
            String localAccountId = !StringHelper.isBlank(idToken.objectIdentifier) ? idToken.objectIdentifier : idToken.subject;
            account.localAccountId(localAccountId);
            account.username(idToken.preferredUsername);
            account.name(idToken.name);
        }

        return account;
    }

    static AccountCacheEntity createADFSAccount(Authority requestAuthority, IdToken idToken) {
        AccountCacheEntity account = new AccountCacheEntity();
        account.authorityType("ADFS");
        account.homeAccountId(idToken.subject);
        account.environment(requestAuthority.host());
        account.username(idToken.upn);
        account.name(idToken.uniqueName);
        return account;
    }

    static AccountCacheEntity create(String clientInfoStr, Authority requestAuthority, IdToken idToken) {
        return create(clientInfoStr, requestAuthority, idToken, (String)null);
    }

    IAccount toAccount() {
        return new Account(this.homeAccountId, this.environment, this.username);
    }

    public String homeAccountId() {
        return this.homeAccountId;
    }

    public String environment() {
        return this.environment;
    }

    public String realm() {
        return this.realm;
    }

    public String localAccountId() {
        return this.localAccountId;
    }

    public String username() {
        return this.username;
    }

    public String name() {
        return this.name;
    }

    public String clientInfoStr() {
        return this.clientInfoStr;
    }

    public String authorityType() {
        return this.authorityType;
    }

    public AccountCacheEntity homeAccountId(String homeAccountId) {
        this.homeAccountId = homeAccountId;
        return this;
    }

    public AccountCacheEntity environment(String environment) {
        this.environment = environment;
        return this;
    }

    public AccountCacheEntity realm(String realm) {
        this.realm = realm;
        return this;
    }

    public AccountCacheEntity localAccountId(String localAccountId) {
        this.localAccountId = localAccountId;
        return this;
    }

    public AccountCacheEntity username(String username) {
        this.username = username;
        return this;
    }

    public AccountCacheEntity name(String name) {
        this.name = name;
        return this;
    }

    public AccountCacheEntity clientInfoStr(String clientInfoStr) {
        this.clientInfoStr = clientInfoStr;
        return this;
    }

    public AccountCacheEntity authorityType(String authorityType) {
        this.authorityType = authorityType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AccountCacheEntity)) {
            return false;
        } else {
            AccountCacheEntity other = (AccountCacheEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95: {
                    Object this$homeAccountId = this.homeAccountId();
                    Object other$homeAccountId = other.homeAccountId();
                    if (this$homeAccountId == null) {
                        if (other$homeAccountId == null) {
                            break label95;
                        }
                    } else if (this$homeAccountId.equals(other$homeAccountId)) {
                        break label95;
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

                Object this$localAccountId = this.localAccountId();
                Object other$localAccountId = other.localAccountId();
                if (this$localAccountId == null) {
                    if (other$localAccountId != null) {
                        return false;
                    }
                } else if (!this$localAccountId.equals(other$localAccountId)) {
                    return false;
                }

                label74: {
                    Object this$username = this.username();
                    Object other$username = other.username();
                    if (this$username == null) {
                        if (other$username == null) {
                            break label74;
                        }
                    } else if (this$username.equals(other$username)) {
                        break label74;
                    }

                    return false;
                }

                label67: {
                    Object this$name = this.name();
                    Object other$name = other.name();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label67;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label67;
                    }

                    return false;
                }

                Object this$clientInfoStr = this.clientInfoStr();
                Object other$clientInfoStr = other.clientInfoStr();
                if (this$clientInfoStr == null) {
                    if (other$clientInfoStr != null) {
                        return false;
                    }
                } else if (!this$clientInfoStr.equals(other$clientInfoStr)) {
                    return false;
                }

                Object this$authorityType = this.authorityType();
                Object other$authorityType = other.authorityType();
                if (this$authorityType == null) {
                    if (other$authorityType != null) {
                        return false;
                    }
                } else if (!this$authorityType.equals(other$authorityType)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountCacheEntity;
    }

    @Override
    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $homeAccountId = this.homeAccountId();
         result = result * 59 + ($homeAccountId == null ? 43 : $homeAccountId.hashCode());
        Object $environment = this.environment();
        result = result * 59 + ($environment == null ? 43 : $environment.hashCode());
        Object $localAccountId = this.localAccountId();
        result = result * 59 + ($localAccountId == null ? 43 : $localAccountId.hashCode());
        Object $username = this.username();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        Object $name = this.name();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $clientInfoStr = this.clientInfoStr();
        result = result * 59 + ($clientInfoStr == null ? 43 : $clientInfoStr.hashCode());
        Object $authorityType = this.authorityType();
        result = result * 59 + ($authorityType == null ? 43 : $authorityType.hashCode());
        return result;
    }
}