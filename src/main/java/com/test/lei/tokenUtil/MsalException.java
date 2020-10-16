package com.test.lei.tokenUtil;

/**
 * @description: MsalException
 * @author: leiming5
 * @date: 2020-10-09 17:32
 */
public class MsalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * Authentication error code
     */
    private String errorCode;

    /**
     * Initializes a new instance of the exception class

     * @param throwable the inner exception that is the cause of the current exception
     */
    public MsalException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Initializes a new instance of the exception class

     * @param message the error message that explains the reason for the exception
     */
    public MsalException(final String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Authentication error code
     */
    @java.lang.SuppressWarnings("all")
    public String errorCode() {
        return this.errorCode;
    }
}
