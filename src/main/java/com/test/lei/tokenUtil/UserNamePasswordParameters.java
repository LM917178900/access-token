package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.Set;

/**
 * @description: UserNamePasswordParameters
 * @author: leiming5
 * @date: 2020-10-14 10:48
 */
public class UserNamePasswordParameters {
    @NonNull
    private Set<String> scopes;
    @NonNull
    private String username;
    @NonNull
    private char[] password;

    public char[] password() {
        return (char[])this.password.clone();
    }

    private static UserNamePasswordParameters.UserNamePasswordParametersBuilder builder() {
        return new UserNamePasswordParameters.UserNamePasswordParametersBuilder();
    }

    public static UserNamePasswordParameters.UserNamePasswordParametersBuilder builder(Set<String> scopes, String username, char[] password) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        ParameterValidationUtils.validateNotBlank("username", username);
        ParameterValidationUtils.validateNotEmpty("password", password);
        return builder().scopes(scopes).username(username).password(password);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public String username() {
        return this.username;
    }

    @ConstructorProperties({"scopes", "username", "password"})
    private UserNamePasswordParameters(@NonNull Set<String> scopes, @NonNull String username, @NonNull char[] password) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else if (username == null) {
            throw new NullPointerException("username is marked @NonNull but is null");
        } else if (password == null) {
            throw new NullPointerException("password is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.username = username;
            this.password = password;
        }
    }

    public static class UserNamePasswordParametersBuilder {
        private Set<String> scopes;
        private String username;
        private char[] password;

        public UserNamePasswordParameters.UserNamePasswordParametersBuilder password(char[] password) {
            this.password = (char[])password.clone();
            return this;
        }

        UserNamePasswordParametersBuilder() {
        }

        public UserNamePasswordParameters.UserNamePasswordParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public UserNamePasswordParameters.UserNamePasswordParametersBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserNamePasswordParameters build() {
            return new UserNamePasswordParameters(this.scopes, this.username, this.password);
        }

        public String toString() {
            return "UserNamePasswordParameters.UserNamePasswordParametersBuilder(scopes=" + this.scopes + ", username=" + this.username + ", password=" + Arrays.toString(this.password) + ")";
        }
    }
}
