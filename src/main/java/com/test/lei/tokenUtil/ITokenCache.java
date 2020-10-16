package com.test.lei.tokenUtil;

/**
 * @description: ITokenCache
 * @author: leiming5
 * @date: 2020-10-09 21:11
 */
public interface ITokenCache {
    /**
     * Deserialize token cache from json
     *
     * @param data serialized cache in json format
     */
    void deserialize(String data);

    /**
     * Serialize token cache to json
     *
     * @return serialized cache in json format
     */
    String serialize();
}
