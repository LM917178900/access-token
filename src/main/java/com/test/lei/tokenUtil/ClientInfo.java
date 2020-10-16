package com.test.lei.tokenUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;

/**
 * @description: ClientInfo
 * @author: leiming5
 * @date: 2020-10-09 21:41
 */
public class ClientInfo {
    @JsonProperty("uid")
    private String uniqueIdentifier;
    @JsonProperty("utid")
    private String unqiueTenantIdentifier;

    ClientInfo() {
    }

    public static ClientInfo createFromJson(String clientInfoJsonBase64Encoded) {
        if (StringHelper.isBlank(clientInfoJsonBase64Encoded)) {
            return null;
        } else {
            byte[] decodedInput = Base64.getDecoder().decode(clientInfoJsonBase64Encoded.getBytes(StandardCharset.UTF_8));
            return (ClientInfo)JsonHelper.convertJsonToObject(new String(decodedInput, StandardCharset.UTF_8), ClientInfo.class);
        }
    }

    String toAccountIdentifier() {
        return this.uniqueIdentifier + "." + this.unqiueTenantIdentifier;
    }

    String getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }

    String getUnqiueTenantIdentifier() {
        return this.unqiueTenantIdentifier;
    }
}
