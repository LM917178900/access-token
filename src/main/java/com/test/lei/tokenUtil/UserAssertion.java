package com.test.lei.tokenUtil;

/**
 * @description: UserAssertion
 * @author: leiming5
 * @date: 2020-10-15 18:59
 */
public class UserAssertion implements IUserAssertion {
    private final String assertion;

    public UserAssertion(String assertion) {
        if (StringHelper.isBlank(assertion)) {
            throw new NullPointerException("assertion");
        } else {
            this.assertion = assertion;
        }
    }

    @Override
    public String getAssertion() {
        return this.assertion;
    }
}
