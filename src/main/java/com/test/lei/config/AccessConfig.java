package com.test.lei.config;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * @description: AccessConfig
 * @author: leiming5
 * @date: 2020-10-16 09:08
 */
public class AccessConfig {

    public static final TreeSet<String> TRUSTED_HOSTS_SET = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    public static final List<String> HOST_LIST = Arrays.asList("login.windows.net", "login.chinacloudapi.cn", "login-us.microsoftonline.com", "login.microsoftonline.de", "login.microsoftonline.com", "login.microsoftonline.us");

    static {
        TRUSTED_HOSTS_SET.addAll(HOST_LIST);
    }
    /**
     * 国外uri
     */
    public static final String DEFAULT_AUTHORITY = "https://login.microsoftonline.com/common/";
    public static final String DEFAULT_TRUSTED_HOST = "login.microsoftonline.com";

    /**
     * 国内uri
     */

}
