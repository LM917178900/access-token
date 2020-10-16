package com.test.lei.tokenUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: HttpResponse
 * @author: leiming5
 * @date: 2020-10-09 21:31
 */
public class HttpResponse implements IHttpResponse {
    /**
     * HTTP response status code
     */
    private int statusCode;
    /**
     * HTTP response headers
     */
    private Map<String, List<String>> headers = new HashMap<>();
    /**
     * HTTP response body
     */
    private String body;

    /**
     * @param responseHeaders Map of HTTP headers returned from HTTP client
     */
    public void headers(Map<String, List<String>> responseHeaders) {
        for (Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            List<String> values = entry.getValue();
            if (values == null || values.isEmpty() || values.get(0) == null) {
                continue;
            }
            header(entry.getKey(), values.toArray(new String[] {}));
        }
    }

    private void header(final String name, final String... values) {
        if (values != null && values.length > 0) {
            headers.put(name, Arrays.asList(values));
        } else {
            headers.remove(name);
        }
    }

    /**
     * HTTP response status code
     */
    @Override
    @java.lang.SuppressWarnings("all")
    public int statusCode() {
        return this.statusCode;
    }

    /**
     * HTTP response headers
     */
    @Override
    @java.lang.SuppressWarnings("all")
    public Map<String, List<String>> headers() {
        return this.headers;
    }

    /**
     * HTTP response body
     */
    @Override
    @java.lang.SuppressWarnings("all")
    public String body() {
        return this.body;
    }

    /**
     * HTTP response status code
     * @return this
     */
    @java.lang.SuppressWarnings("all")
    public HttpResponse statusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * HTTP response body
     * @return this
     */
    @java.lang.SuppressWarnings("all")
    public HttpResponse body(final String body) {
        this.body = body;
        return this;
    }
}
