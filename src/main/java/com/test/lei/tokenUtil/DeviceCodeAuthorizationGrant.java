package com.test.lei.tokenUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: DeviceCodeAuthorizationGrant
 * @author: leiming5
 * @date: 2020-10-15 18:56
 */
class DeviceCodeAuthorizationGrant extends AbstractMsalAuthorizationGrant {
    private static final String GRANT_TYPE = "device_code";
    private final DeviceCode deviceCode;
    private final String scopes;
    private String correlationId;

    DeviceCodeAuthorizationGrant(DeviceCode deviceCode, String scopes) {
        this.deviceCode = deviceCode;
        this.correlationId = deviceCode.correlationId();
        this.scopes = scopes;
    }

    public Map<String, List<String>> toParameters() {
        Map<String, List<String>> outParams = new LinkedHashMap();
        outParams.put("scope", Collections.singletonList("openid profile offline_access " + this.scopes));
        outParams.put("grant_type", Collections.singletonList("device_code"));
        outParams.put("device_code", Collections.singletonList(this.deviceCode.deviceCode()));
        outParams.put("client_info", Collections.singletonList("1"));
        return outParams;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }
}
