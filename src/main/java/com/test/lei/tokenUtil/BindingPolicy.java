package com.test.lei.tokenUtil;

/**
 * @description: BindingPolicy
 * @author: leiming5
 * @date: 2020-10-14 10:11
 */
class BindingPolicy {
    private String value;
    private String url;
    private WSTrustVersion version;

    public BindingPolicy(String value) {
        this.value = value;
    }

    public BindingPolicy(String url, WSTrustVersion version) {
        this.url = url;
        this.version = version;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(WSTrustVersion version) {
        this.version = version;
    }

    public WSTrustVersion getVersion() {
        return this.version;
    }
}
