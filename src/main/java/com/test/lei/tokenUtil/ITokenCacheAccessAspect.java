package com.test.lei.tokenUtil;

public interface ITokenCacheAccessAspect {
    void beforeCacheAccess(ITokenCacheAccessContext var1);

    void afterCacheAccess(ITokenCacheAccessContext var1);
}