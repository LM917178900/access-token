package com.test.lei.tokenUtil;

import java.util.List;
import java.util.Map;

/**
 * @description: HttpUtils
 * @author: leiming5 todo
 * @date: 2020-10-09 18:07
 */
public class HttpUtils {

    HttpUtils() {
    }

    static String headerValue(Map<String, List<String>> headers, String headerName) {
        if (headerName != null && headers != null) {
            List<String> headerValue = (List)headers.get(headerName);
            return headerValue != null && !headerValue.isEmpty() ? String.join(",", headerValue) : null;
        } else {
            return null;
        }
    }
}
