package com.test.lei.tokenUtil;

/**
 * @description: LogHelper
 * @author: leiming5
 * @date: 2020-10-09 17:45
 */
final class LogHelper {

    LogHelper() {
    }

    static String createMessage(String originalMessage, String correlationId) {
        return String.format("[Correlation ID: %s] " + originalMessage, correlationId);
    }

    static String getPiiScrubbedDetails(Throwable ex) {
        if (ex == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(ex.getClass().getName());
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            StackTraceElement[] var3 = stackTraceElements;
            int var4 = stackTraceElements.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                StackTraceElement traceElement = var3[var5];
                sb.append(System.getProperty("line.separator") + "\tat " + traceElement);
            }

            if (ex.getCause() != null) {
                sb.append(System.getProperty("line.separator") + "Caused by: " + getPiiScrubbedDetails(ex.getCause()));
            }

            return sb.toString();
        }
    }
}
