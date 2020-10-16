package com.test.lei.tokenUtil;

import java.beans.ConstructorProperties;

/**
 * @description: Account
 * @author: leiming5 todo
 * @date: 2020-10-09 21:39
 */
public class Account implements IAccount {
    String homeAccountId;
    String environment;
    String username;

    @Override
    public String homeAccountId() {
        return this.homeAccountId;
    }

    @Override
    public String environment() {
        return this.environment;
    }

    @Override
    public String username() {
        return this.username;
    }

    public Account homeAccountId(String homeAccountId) {
        this.homeAccountId = homeAccountId;
        return this;
    }

    public Account environment(String environment) {
        this.environment = environment;
        return this;
    }

    public Account username(String username) {
        this.username = username;
        return this;
    }

    @ConstructorProperties({"homeAccountId", "environment", "username"})
    public Account(String homeAccountId, String environment, String username) {
        this.homeAccountId = homeAccountId;
        this.environment = environment;
        this.username = username;
    }
}
