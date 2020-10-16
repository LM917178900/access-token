package com.test.lei.tokenUtil;

/**
 * @description: SerializeException
 * @author: leiming5
 * @date: 2020-10-10 09:31
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        this(message, (Throwable)null);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
