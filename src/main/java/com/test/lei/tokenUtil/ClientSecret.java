package com.test.lei.tokenUtil;


/**
 * @description: ClientSecret
 * @author: leiming5
 * @date: 2020-10-09 10:27
 */
public class ClientSecret implements IClientSecret {

    private final String clientSecret;

    ClientSecret(String clientSecret) {
        if (StringHelper.isBlank(clientSecret)) {
            throw new IllegalArgumentException("clientSecret is null or empty");
        } else {
            this.clientSecret = clientSecret;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ClientSecret)) {
            return false;
        } else {
            ClientSecret other = (ClientSecret)o;
            Object this$clientSecret = this.clientSecret();
            Object other$clientSecret = other.clientSecret();
            if (this$clientSecret == null) {
                if (other$clientSecret != null) {
                    return false;
                }
            } else if (!this$clientSecret.equals(other$clientSecret)) {
                return false;
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $clientSecret = this.clientSecret();
         result = result * 59 + ($clientSecret == null ? 43 : $clientSecret.hashCode());
        return result;
    }

    @Override
    public String clientSecret() {
        return this.clientSecret;
    }
}
