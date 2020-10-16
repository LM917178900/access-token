package com.test.lei.tokenUtil;

/**
 * @description: IClientAssertion
 * @author: leiming5
 * @date: 2020-10-15 19:01
 */
public interface IClientAssertion extends IClientCredential{

    /**
     * @return Jwt token encoded as a base64 URL encoded string
     */
    String assertion();
}
