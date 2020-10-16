package com.test.lei.tokenUtil;

/**
 * @description: ClientAssertion
 * @author: leiming5
 * @date: 2020-10-15 19:01
 */
final class ClientAssertion implements IClientAssertion {
    static final String assertionType = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
    private final String assertion;

    ClientAssertion(String assertion) {
        if (StringHelper.isBlank(assertion)) {
            throw new NullPointerException("assertion");
        } else {
            this.assertion = assertion;
        }
    }

    @Override
    public String assertion() {
        return this.assertion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ClientAssertion)) {
            return false;
        } else {
            ClientAssertion other = (ClientAssertion)o;
            Object this$assertion = this.assertion();
            Object other$assertion = other.assertion();
            if (this$assertion == null) {
                if (other$assertion != null) {
                    return false;
                }
            } else if (!this$assertion.equals(other$assertion)) {
                return false;
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $assertion = this.assertion();
        result = result * 59 + ($assertion == null ? 43 : $assertion.hashCode());
        return result;
    }
}

