package com.test.lei.tokenUtil;

/**
 * @description: ClientCredentialFactory
 * @author: leiming5
 * @date: 2020-10-09 10:17
 */
public class ClientCredentialFactory {

    public static IClientSecret createFromSecret(String secret){
        return new ClientSecret(secret);
    }
}
