package com.test.lei.tokenUtil;

/**
 * @description: StringHelper
 * @author: leiming5
 * @date: 2020-10-09 17:44
 */
final class StringHelper {

    public static boolean isBlank(final String str) {
        return str == null || str.trim().length() == 0;
    }

}
