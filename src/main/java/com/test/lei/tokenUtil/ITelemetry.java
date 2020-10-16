package com.test.lei.tokenUtil;

/**
 * @description: ITelemetry
 * @author: leiming5
 * @date: 2020-10-09 17:49
 */
public interface ITelemetry {

    void startEvent(String var1, Event var2);

    void stopEvent(String var1, Event var2);

    void flush(String var1, String var2);
}
