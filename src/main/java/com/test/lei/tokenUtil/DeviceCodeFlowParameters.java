package com.test.lei.tokenUtil;

import org.springframework.lang.NonNull;

import java.beans.ConstructorProperties;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @description: DeviceCodeFlowParameters
 * @author: leiming5
 * @date: 2020-10-09 13:17
 */
public class DeviceCodeFlowParameters {
    @NonNull
    private Set<String> scopes;
    @NonNull
    private Consumer<DeviceCode> deviceCodeConsumer;

    private static DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder builder() {
        return new DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder();
    }

    public static DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder builder(Set<String> scopes, Consumer<DeviceCode> deviceCodeConsumer) {
        ParameterValidationUtils.validateNotEmpty("scopes", scopes);
        return builder().scopes(scopes).deviceCodeConsumer(deviceCodeConsumer);
    }

    @NonNull
    public Set<String> scopes() {
        return this.scopes;
    }

    @NonNull
    public Consumer<DeviceCode> deviceCodeConsumer() {
        return this.deviceCodeConsumer;
    }

    @ConstructorProperties({"scopes", "deviceCodeConsumer"})
    private DeviceCodeFlowParameters(@NonNull Set<String> scopes, @NonNull Consumer<DeviceCode> deviceCodeConsumer) {
        if (scopes == null) {
            throw new NullPointerException("scopes is marked @NonNull but is null");
        } else if (deviceCodeConsumer == null) {
            throw new NullPointerException("deviceCodeConsumer is marked @NonNull but is null");
        } else {
            this.scopes = scopes;
            this.deviceCodeConsumer = deviceCodeConsumer;
        }
    }

    public static class DeviceCodeFlowParametersBuilder {
        private Set<String> scopes;
        private Consumer<DeviceCode> deviceCodeConsumer;

        DeviceCodeFlowParametersBuilder() {
        }

        public DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder scopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder deviceCodeConsumer(Consumer<DeviceCode> deviceCodeConsumer) {
            this.deviceCodeConsumer = deviceCodeConsumer;
            return this;
        }

        public DeviceCodeFlowParameters build() {
            return new DeviceCodeFlowParameters(this.scopes, this.deviceCodeConsumer);
        }

        @Override
        public String toString() {
            return "DeviceCodeFlowParameters.DeviceCodeFlowParametersBuilder(scopes=" + this.scopes + ", deviceCodeConsumer=" + this.deviceCodeConsumer + ")";
        }
    }
}
