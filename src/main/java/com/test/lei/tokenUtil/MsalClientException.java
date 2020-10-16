package com.test.lei.tokenUtil;

/**
 * @description: MsalClientException
 * @author: leiming5
 * @date: 2020-10-09 17:38
 */
public class MsalClientException extends MsalException {
    public MsalClientException(Throwable throwable) {
        super(throwable);
    }

    public MsalClientException(String message, String errorCode) {
        super(message, errorCode);
    }
}
