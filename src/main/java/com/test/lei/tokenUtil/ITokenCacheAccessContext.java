package com.test.lei.tokenUtil;

public interface ITokenCacheAccessContext {

    /**
     * @return instance of accessed ITokenCache
     */
    ITokenCache tokenCache();

    /**
     * @return client id used for cache access
     */
    String clientId();

    /**
     * @return instance of IAccount used for cache access
     */
    IAccount account();

    /**
     * @return a boolean value telling whether cache was changed
     */
    boolean hasCacheChanged();
}
