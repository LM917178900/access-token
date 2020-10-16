package com.test.lei.tokenUtil;

import java.util.Objects;

/**
 * @description: EventKey
 * @author: leiming5
 * @date: 2020-10-09 18:15
 */
public class EventKey {

    private String requestId;
    private String eventName;

    EventKey(String requestId, Event event) {
        this.requestId = requestId;
        this.eventName = (String)event.get("event_name");
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getEventName() {
        return this.eventName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof EventKey)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            EventKey eventKey = (EventKey)obj;
            return Objects.equals(this.requestId, eventKey.getRequestId()) && Objects.equals(this.eventName, eventKey.getEventName());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{this.requestId, this.eventName});
    }
}
