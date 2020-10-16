package com.test.lei.tokenUtil;

/**
 * @description: TelemetryHelper
 * @author: leiming5
 * @date: 2020-10-09 17:48
 */
public class TelemetryHelper implements AutoCloseable {
    private Event eventToEnd;
    private String requestId;
    private String clientId;
    private ITelemetry telemetry;
    private Boolean shouldFlush;

    TelemetryHelper(ITelemetry telemetry, String requestId, String clientId, Event event, Boolean shouldFlush) {
        this.telemetry = telemetry;
        this.requestId = requestId;
        this.clientId = clientId;
        this.eventToEnd = event;
        this.shouldFlush = shouldFlush;
        if (telemetry != null) {
            telemetry.startEvent(requestId, event);
        }

    }

    @Override
    public void close() {
        if (this.telemetry != null) {
            this.telemetry.stopEvent(this.requestId, this.eventToEnd);
            if (this.shouldFlush) {
                this.telemetry.flush(this.requestId, this.clientId);
            }
        }

    }
}
