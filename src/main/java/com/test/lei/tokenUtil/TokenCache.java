package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description: TokenCache
 * @author: leiming5
 * @date: 2020-10-09 17:15
 */
public class TokenCache implements ITokenCache {
    protected static final int MIN_ACCESS_TOKEN_EXPIRE_IN_SEC = 300;
    private transient ReadWriteLock lock;
    @JsonProperty("AccessToken")
    Map<String, AccessTokenCacheEntity> accessTokens;
    @JsonProperty("RefreshToken")
    Map<String, RefreshTokenCacheEntity> refreshTokens;
    @JsonProperty("IdToken")
    Map<String, IdTokenCacheEntity> idTokens;
    @JsonProperty("Account")
    Map<String, AccountCacheEntity> accounts;
    @JsonProperty("AppMetadata")
    Map<String, AppMetadataCacheEntity> appMetadata;
    transient ITokenCacheAccessAspect tokenCacheAccessAspect;
    private transient String serializedCachedSnapshot;

    public TokenCache(ITokenCacheAccessAspect tokenCacheAccessAspect) {
        this();
        this.tokenCacheAccessAspect = tokenCacheAccessAspect;
    }

    public TokenCache() {
        this.lock = new ReentrantReadWriteLock();
        this.accessTokens = new LinkedHashMap();
        this.refreshTokens = new LinkedHashMap();
        this.idTokens = new LinkedHashMap();
        this.accounts = new LinkedHashMap();
        this.appMetadata = new LinkedHashMap();
    }

    @Override
    public void deserialize(String data) {
        if (!StringHelper.isBlank(data)) {
            this.serializedCachedSnapshot = data;
            TokenCache deserializedCache = (TokenCache)JsonHelper.convertJsonToObject(data, TokenCache.class);
            this.lock.writeLock().lock();

            try {
                this.accessTokens = deserializedCache.accessTokens;
                this.accounts = deserializedCache.accounts;
                this.refreshTokens = deserializedCache.refreshTokens;
                this.idTokens = deserializedCache.idTokens;
                this.appMetadata = deserializedCache.appMetadata;
            } finally {
                this.lock.writeLock().unlock();
            }

        }
    }

    private static void mergeJsonObjects(JsonNode old, JsonNode update) {
        mergeRemovals(old, update);
        mergeUpdates(old, update);
    }

    private static void mergeUpdates(JsonNode old, JsonNode update) {
        Iterator fieldNames = update.fieldNames();

        while(true) {
            String uKey;
            JsonNode uValue;
            do {
                label26:
                do {
                    while(fieldNames.hasNext()) {
                        uKey = (String)fieldNames.next();
                        uValue = update.get(uKey);
                        if (!old.has(uKey)) {
                            continue label26;
                        }

                        JsonNode oValue = old.get(uKey);
                        if (uValue.isObject()) {
                            mergeUpdates(oValue, uValue);
                        } else {
                            ((ObjectNode)old).set(uKey, uValue);
                        }
                    }

                    return;
                } while(uValue.isNull());
            } while(uValue.isObject() && uValue.size() == 0);

            ((ObjectNode)old).set(uKey, uValue);
        }
    }

    private static void mergeRemovals(JsonNode old, JsonNode update) {
        Set<String> msalEntities = new HashSet(Arrays.asList("Account", "AccessToken", "RefreshToken", "IdToken", "AppMetadata"));
        Iterator var3 = msalEntities.iterator();

        label32:
        while(true) {
            JsonNode oldEntries;
            JsonNode newEntries;
            do {
                if (!var3.hasNext()) {
                    return;
                }

                String msalEntity = (String)var3.next();
                oldEntries = old.get(msalEntity);
                newEntries = update.get(msalEntity);
            } while(oldEntries == null);

            Iterator iterator = oldEntries.fields();

            while(true) {
                String key;
                do {
                    if (!iterator.hasNext()) {
                        continue label32;
                    }

                    Map.Entry<String, JsonNode> oEntry = (Map.Entry)iterator.next();
                    key = (String)oEntry.getKey();
                } while(newEntries != null && newEntries.has(key));

                iterator.remove();
            }
        }
    }

    @Override
    public String serialize() {
        this.lock.readLock().lock();

        String var3;
        try {
            if (StringHelper.isBlank(this.serializedCachedSnapshot)) {
                String var9 = JsonHelper.mapper.writeValueAsString(this);
                return var9;
            }

            JsonNode cache = JsonHelper.mapper.readTree(this.serializedCachedSnapshot);
            JsonNode update = JsonHelper.mapper.valueToTree(this);
            mergeJsonObjects(cache, update);
            var3 = JsonHelper.mapper.writeValueAsString(cache);
        } catch (JsonProcessingException var7) {
            throw new MsalClientException(var7);
        } finally {
            this.lock.readLock().unlock();
        }

        return var3;
    }

    void saveTokens(TokenRequestExecutor tokenRequestExecutor, AuthenticationResult authenticationResult, String environment) {
        TokenCache.CacheAspect cacheAspect = new TokenCache.CacheAspect(TokenCacheAccessContext.builder().clientId(tokenRequestExecutor.getMsalRequest().application().clientId()).tokenCache(this).hasCacheChanged(true).build());
        Throwable var5 = null;

        try {
            try {
                this.lock.writeLock().lock();
                if (!StringHelper.isBlank(authenticationResult.accessToken())) {
                    AccessTokenCacheEntity atEntity = createAccessTokenCacheEntity(tokenRequestExecutor, authenticationResult, environment);
                    this.accessTokens.put(atEntity.getKey(), atEntity);
                }

                if (!StringHelper.isBlank(authenticationResult.familyId())) {
                    AppMetadataCacheEntity appMetadataCacheEntity = createAppMetadataCacheEntity(tokenRequestExecutor, authenticationResult, environment);
                    this.appMetadata.put(appMetadataCacheEntity.getKey(), appMetadataCacheEntity);
                }

                if (!StringHelper.isBlank(authenticationResult.refreshToken())) {
                    RefreshTokenCacheEntity rtEntity = createRefreshTokenCacheEntity(tokenRequestExecutor, authenticationResult, environment);
                    rtEntity.family_id(authenticationResult.familyId());
                    this.refreshTokens.put(rtEntity.getKey(), rtEntity);
                }

                if (!StringHelper.isBlank(authenticationResult.idToken())) {
                    IdTokenCacheEntity idTokenEntity = createIdTokenCacheEntity(tokenRequestExecutor, authenticationResult, environment);
                    this.idTokens.put(idTokenEntity.getKey(), idTokenEntity);
                    AccountCacheEntity accountCacheEntity = authenticationResult.accountCacheEntity();
                    accountCacheEntity.environment(environment);
                    this.accounts.put(accountCacheEntity.getKey(), accountCacheEntity);
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Throwable var22) {
            var5 = var22;
            throw var22;
        } finally {
            if (cacheAspect != null) {
                if (var5 != null) {
                    try {
                        cacheAspect.close();
                    } catch (Throwable var20) {
                        var5.addSuppressed(var20);
                    }
                } else {
                    cacheAspect.close();
                }
            }

        }

    }

    private static RefreshTokenCacheEntity createRefreshTokenCacheEntity(TokenRequestExecutor tokenRequestExecutor, AuthenticationResult authenticationResult, String environmentAlias) {
        RefreshTokenCacheEntity rt = new RefreshTokenCacheEntity();
        rt.credentialType(CredentialTypeEnum.REFRESH_TOKEN.value());
        if (authenticationResult.account() != null) {
            rt.homeAccountId(authenticationResult.account().homeAccountId());
        }

        rt.environment(environmentAlias);
        rt.clientId(tokenRequestExecutor.getMsalRequest().application().clientId());
        rt.secret(authenticationResult.refreshToken());
        return rt;
    }

    private static AccessTokenCacheEntity createAccessTokenCacheEntity(TokenRequestExecutor tokenRequestExecutor, AuthenticationResult authenticationResult, String environmentAlias) {
        AccessTokenCacheEntity at = new AccessTokenCacheEntity();
        at.credentialType(CredentialTypeEnum.ACCESS_TOKEN.value());
        if (authenticationResult.account() != null) {
            at.homeAccountId(authenticationResult.account().homeAccountId());
        }

        at.environment(environmentAlias);
        at.clientId(tokenRequestExecutor.getMsalRequest().application().clientId());
        at.secret(authenticationResult.accessToken());
        at.realm(tokenRequestExecutor.requestAuthority.tenant());
        String scopes = !StringHelper.isBlank(authenticationResult.scopes()) ? authenticationResult.scopes() : tokenRequestExecutor.getMsalRequest().msalAuthorizationGrant().getScopes();
        at.target(scopes);
        long currTimestampSec = System.currentTimeMillis() / 1000L;
        at.cachedAt(Long.toString(currTimestampSec));
        at.expiresOn(Long.toString(authenticationResult.expiresOn()));
        if (authenticationResult.extExpiresOn() > 0L) {
            at.extExpiresOn(Long.toString(authenticationResult.extExpiresOn()));
        }

        return at;
    }

    private static IdTokenCacheEntity createIdTokenCacheEntity(TokenRequestExecutor tokenRequestExecutor, AuthenticationResult authenticationResult, String environmentAlias) {
        IdTokenCacheEntity idToken = new IdTokenCacheEntity();
        idToken.credentialType(CredentialTypeEnum.ID_TOKEN.value());
        if (authenticationResult.account() != null) {
            idToken.homeAccountId(authenticationResult.account().homeAccountId());
        }

        idToken.environment(environmentAlias);
        idToken.clientId(tokenRequestExecutor.getMsalRequest().application().clientId());
        idToken.secret(authenticationResult.idToken());
        idToken.realm(tokenRequestExecutor.requestAuthority.tenant());
        return idToken;
    }

    private static AppMetadataCacheEntity createAppMetadataCacheEntity(TokenRequestExecutor tokenRequestExecutor, AuthenticationResult authenticationResult, String environmentAlias) {
        AppMetadataCacheEntity appMetadataCacheEntity = new AppMetadataCacheEntity();
        appMetadataCacheEntity.clientId(tokenRequestExecutor.getMsalRequest().application().clientId());
        appMetadataCacheEntity.environment(environmentAlias);
        appMetadataCacheEntity.familyId(authenticationResult.familyId());
        return appMetadataCacheEntity;
    }

    Set<IAccount> getAccounts(String clientId, Set<String> environmentAliases) {
        TokenCache.CacheAspect cacheAspect = new TokenCache.CacheAspect(TokenCacheAccessContext.builder().clientId(clientId).tokenCache(this).build());
        Throwable var4 = null;

        Set var5;
        try {
            try {
                this.lock.readLock().lock();
                var5 = (Set)this.accounts.values().stream().filter((acc) -> {
                    return environmentAliases.contains(acc.environment());
                }).collect(Collectors.mapping(AccountCacheEntity::toAccount, Collectors.toSet()));
            } finally {
                this.lock.readLock().unlock();
            }
        } catch (Throwable var21) {
            var4 = var21;
            throw var21;
        } finally {
            if (cacheAspect != null) {
                if (var4 != null) {
                    try {
                        cacheAspect.close();
                    } catch (Throwable var19) {
                        var4.addSuppressed(var19);
                    }
                } else {
                    cacheAspect.close();
                }
            }

        }

        return var5;
    }

    private String getApplicationFamilyId(String clientId, Set<String> environmentAliases) {
        Iterator var3 = this.appMetadata.values().iterator();

        AppMetadataCacheEntity data;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            data = (AppMetadataCacheEntity)var3.next();
        } while(!data.clientId().equals(clientId) || !environmentAliases.contains(data.environment()) || StringHelper.isBlank(data.familyId()));

        return data.familyId();
    }

    private Set<String> getFamilyClientIds(String familyId, Set<String> environmentAliases) {
        return (Set)this.appMetadata.values().stream().filter((appMetadata) -> {
            return environmentAliases.contains(appMetadata.environment()) && familyId.equals(appMetadata.familyId());
        }).map(AppMetadataCacheEntity::clientId).collect(Collectors.toSet());
    }

    void removeAccount(String clientId, IAccount account, Set<String> environmentAliases) {
        TokenCache.CacheAspect cacheAspect = new TokenCache.CacheAspect(TokenCacheAccessContext.builder().clientId(clientId).tokenCache(this).hasCacheChanged(true).build());
        Throwable var5 = null;

        try {
            try {
                this.lock.writeLock().lock();
                this.removeAccount(account, environmentAliases);
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Throwable var20) {
            var5 = var20;
            throw var20;
        } finally {
            if (cacheAspect != null) {
                if (var5 != null) {
                    try {
                        cacheAspect.close();
                    } catch (Throwable var18) {
                        var5.addSuppressed(var18);
                    }
                } else {
                    cacheAspect.close();
                }
            }

        }

    }

    private void removeAccount(IAccount account, Set<String> environmentAliases) {
        Predicate<Map.Entry<String, ? extends Credential>> credentialToRemovePredicate = (e) -> {
            return !StringHelper.isBlank(((Credential)e.getValue()).homeAccountId()) && !StringHelper.isBlank(((Credential)e.getValue()).environment()) && ((Credential)e.getValue()).homeAccountId().equals(account.homeAccountId()) && environmentAliases.contains(((Credential)e.getValue()).environment());
        };
        this.accessTokens.entrySet().removeIf(credentialToRemovePredicate);
        this.refreshTokens.entrySet().removeIf(credentialToRemovePredicate);
        this.idTokens.entrySet().removeIf(credentialToRemovePredicate);
        this.accounts.entrySet().removeIf((e) -> {
            return !StringHelper.isBlank(((AccountCacheEntity)e.getValue()).homeAccountId()) && !StringHelper.isBlank(((AccountCacheEntity)e.getValue()).environment()) && ((AccountCacheEntity)e.getValue()).homeAccountId().equals(account.homeAccountId()) && environmentAliases.contains(((AccountCacheEntity)e.getValue()).environment);
        });
    }

    private boolean isMatchingScopes(AccessTokenCacheEntity accessTokenCacheEntity, Set<String> scopes) {
        Set<String> accessTokenCacheEntityScopes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        accessTokenCacheEntityScopes.addAll(Arrays.asList(accessTokenCacheEntity.target().split(" ")));
        return accessTokenCacheEntityScopes.containsAll(scopes);
    }

    private Optional<AccessTokenCacheEntity> getAccessTokenCacheEntity(IAccount account, Authority authority, Set<String> scopes, String clientId, Set<String> environmentAliases) {
        long currTimeStampSec = (new Date()).getTime() / 1000L;
        return this.accessTokens.values().stream().filter((accessToken) -> {
            return accessToken.homeAccountId.equals(account.homeAccountId()) && environmentAliases.contains(accessToken.environment) && Long.parseLong(accessToken.expiresOn()) > currTimeStampSec + 300L && accessToken.realm.equals(authority.tenant()) && accessToken.clientId.equals(clientId) && this.isMatchingScopes(accessToken, scopes);
        }).findAny();
    }

    private Optional<AccessTokenCacheEntity> getApplicationAccessTokenCacheEntity(Authority authority, Set<String> scopes, String clientId, Set<String> environmentAliases) {
        long currTimeStampSec = (new Date()).getTime() / 1000L;
        return this.accessTokens.values().stream().filter((accessToken) -> {
            return environmentAliases.contains(accessToken.environment) && Long.parseLong(accessToken.expiresOn()) > currTimeStampSec + 300L && accessToken.realm.equals(authority.tenant()) && accessToken.clientId.equals(clientId) && this.isMatchingScopes(accessToken, scopes);
        }).findAny();
    }

    private Optional<IdTokenCacheEntity> getIdTokenCacheEntity(IAccount account, Authority authority, String clientId, Set<String> environmentAliases) {
        return this.idTokens.values().stream().filter((idToken) -> {
            return idToken.homeAccountId.equals(account.homeAccountId()) && environmentAliases.contains(idToken.environment) && idToken.realm.equals(authority.tenant()) && idToken.clientId.equals(clientId);
        }).findAny();
    }

    private Optional<RefreshTokenCacheEntity> getRefreshTokenCacheEntity(IAccount account, String clientId, Set<String> environmentAliases) {
        return this.refreshTokens.values().stream().filter((refreshToken) -> {
            return refreshToken.homeAccountId.equals(account.homeAccountId()) && environmentAliases.contains(refreshToken.environment) && refreshToken.clientId.equals(clientId);
        }).findAny();
    }

    private Optional<AccountCacheEntity> getAccountCacheEntity(IAccount account, Set<String> environmentAliases) {
        return this.accounts.values().stream().filter((acc) -> {
            return acc.homeAccountId.equals(account.homeAccountId()) && environmentAliases.contains(acc.environment);
        }).findAny();
    }

    private Optional<RefreshTokenCacheEntity> getAnyFamilyRefreshTokenCacheEntity(IAccount account, Set<String> environmentAliases) {
        return this.refreshTokens.values().stream().filter((refreshToken) -> {
            return refreshToken.homeAccountId.equals(account.homeAccountId()) && environmentAliases.contains(refreshToken.environment) && refreshToken.isFamilyRT();
        }).findAny();
    }

    AuthenticationResult getCachedAuthenticationResult(IAccount account, Authority authority, Set<String> scopes, String clientId) {
        AuthenticationResult.AuthenticationResultBuilder builder = AuthenticationResult.builder();
        builder.environment(authority.host());
        Set<String> environmentAliases = AadInstanceDiscoveryProvider.getAliases(account.environment());
        TokenCache.CacheAspect cacheAspect = new TokenCache.CacheAspect(TokenCacheAccessContext.builder().clientId(clientId).tokenCache(this).account(account).build());
        Throwable var8 = null;

        try {
            try {
                this.lock.readLock().lock();
                Optional<AccountCacheEntity> accountCacheEntity = this.getAccountCacheEntity(account, environmentAliases);
                Optional<AccessTokenCacheEntity> atCacheEntity = this.getAccessTokenCacheEntity(account, authority, scopes, clientId, environmentAliases);
                Optional<IdTokenCacheEntity> idTokenCacheEntity = this.getIdTokenCacheEntity(account, authority, clientId, environmentAliases);
                Optional rtCacheEntity;
                if (!StringHelper.isBlank(this.getApplicationFamilyId(clientId, environmentAliases))) {
                    rtCacheEntity = this.getAnyFamilyRefreshTokenCacheEntity(account, environmentAliases);
                    if (!rtCacheEntity.isPresent()) {
                        rtCacheEntity = this.getRefreshTokenCacheEntity(account, clientId, environmentAliases);
                    }
                } else {
                    rtCacheEntity = this.getRefreshTokenCacheEntity(account, clientId, environmentAliases);
                    if (!rtCacheEntity.isPresent()) {
                        rtCacheEntity = this.getAnyFamilyRefreshTokenCacheEntity(account, environmentAliases);
                    }
                }

                if (atCacheEntity.isPresent()) {
                    builder.accessToken(((AccessTokenCacheEntity)atCacheEntity.get()).secret).expiresOn(Long.parseLong(((AccessTokenCacheEntity)atCacheEntity.get()).expiresOn()));
                }

                if (idTokenCacheEntity.isPresent()) {
                    builder.idToken(((IdTokenCacheEntity)idTokenCacheEntity.get()).secret);
                }

                if (rtCacheEntity.isPresent()) {
                    builder.refreshToken(((RefreshTokenCacheEntity)rtCacheEntity.get()).secret);
                }

                if (accountCacheEntity.isPresent()) {
                    builder.accountCacheEntity((AccountCacheEntity)accountCacheEntity.get());
                }
            } finally {
                this.lock.readLock().unlock();
            }
        } catch (Throwable var27) {
            var8 = var27;
            throw var27;
        } finally {
            if (cacheAspect != null) {
                if (var8 != null) {
                    try {
                        cacheAspect.close();
                    } catch (Throwable var25) {
                        var8.addSuppressed(var25);
                    }
                } else {
                    cacheAspect.close();
                }
            }

        }

        return builder.build();
    }

    AuthenticationResult getCachedAuthenticationResult(Authority authority, Set<String> scopes, String clientId) {
        AuthenticationResult.AuthenticationResultBuilder builder = AuthenticationResult.builder();
        Set<String> environmentAliases = AadInstanceDiscoveryProvider.getAliases(authority.host);
        builder.environment(authority.host());
        TokenCache.CacheAspect cacheAspect = new TokenCache.CacheAspect(TokenCacheAccessContext.builder().clientId(clientId).tokenCache(this).build());
        Throwable var7 = null;

        AuthenticationResult var25;
        try {
            try {
                this.lock.readLock().lock();
                Optional<AccessTokenCacheEntity> atCacheEntity = this.getApplicationAccessTokenCacheEntity(authority, scopes, clientId, environmentAliases);
                if (atCacheEntity.isPresent()) {
                    builder.accessToken(((AccessTokenCacheEntity)atCacheEntity.get()).secret).expiresOn(Long.parseLong(((AccessTokenCacheEntity)atCacheEntity.get()).expiresOn()));
                }
            } finally {
                this.lock.readLock().unlock();
            }

            var25 = builder.build();
        } catch (Throwable var23) {
            var7 = var23;
            throw var23;
        } finally {
            if (cacheAspect != null) {
                if (var7 != null) {
                    try {
                        cacheAspect.close();
                    } catch (Throwable var21) {
                        var7.addSuppressed(var21);
                    }
                } else {
                    cacheAspect.close();
                }
            }

        }

        return var25;
    }

    private class CacheAspect implements AutoCloseable {
        ITokenCacheAccessContext context;

        CacheAspect(ITokenCacheAccessContext context) {
            if (TokenCache.this.tokenCacheAccessAspect != null) {
                this.context = context;
                TokenCache.this.tokenCacheAccessAspect.beforeCacheAccess(context);
            }

        }

        @Override
        public void close() {
            if (TokenCache.this.tokenCacheAccessAspect != null) {
                TokenCache.this.tokenCacheAccessAspect.afterCacheAccess(this.context);
            }

        }
    }
}
