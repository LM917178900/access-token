package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;

public interface IHttpResponse {
    /**
     * @return HTTP response status code.
     */
    int statusCode();

    /**
     *
     * @return HTTP response headers
     */
    Map<String, List<String>> headers();

    /**
     * @return HTTP response body
     */
    String body();
}
