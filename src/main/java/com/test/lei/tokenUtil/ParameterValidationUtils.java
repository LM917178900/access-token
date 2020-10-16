package com.test.lei.tokenUtil;

import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @description: ParameterValidationUtils
 * @author: leiming5
 * @date: 2020-10-09 10:47
 */
public class ParameterValidationUtils {
    static void validateNotBlank(String name, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }

    static void validateNotNull(String name, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    static void validateNotEmpty(String name, Set<String> set) {
        if (set == null || set.isEmpty()) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }

    static void validateNotEmpty(String name, char[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }
}
