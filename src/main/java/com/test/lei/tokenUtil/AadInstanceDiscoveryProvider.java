package com.test.lei.tokenUtil;

import com.test.lei.config.AccessConfig;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: AadInstanceDiscoveryProvider
 * @author: leiming5
 * @date: 2020-10-09 17:21
 */
public class AadInstanceDiscoveryProvider {


    private static final String DEFAULT_TRUSTED_HOST = AccessConfig.DEFAULT_TRUSTED_HOST;
    private static final String AUTHORIZE_ENDPOINT_TEMPLATE = "https://{host}/{tenant}/oauth2/v2.0/authorize";
    private static final String INSTANCE_DISCOVERY_ENDPOINT_TEMPLATE = "https://{host}/common/discovery/instance";
    private static final String INSTANCE_DISCOVERY_REQUEST_PARAMETERS_TEMPLATE = "?api-version=1.1&authorization_endpoint={authorizeEndpoint}";
    static final TreeSet<String> TRUSTED_HOSTS_SET = AccessConfig.TRUSTED_HOSTS_SET;
    static ConcurrentHashMap<String, InstanceDiscoveryMetadataEntry> cache = new ConcurrentHashMap();

    AadInstanceDiscoveryProvider() {
    }

    static InstanceDiscoveryMetadataEntry getMetadataEntry(URL authorityUrl, boolean validateAuthority, MsalRequest msalRequest, ServiceBundle serviceBundle) {
        InstanceDiscoveryMetadataEntry result = (InstanceDiscoveryMetadataEntry)cache.get(authorityUrl.getAuthority());
        if (result == null) {
            doInstanceDiscoveryAndCache(authorityUrl, validateAuthority, msalRequest, serviceBundle);
        }

        return (InstanceDiscoveryMetadataEntry)cache.get(authorityUrl.getAuthority());
    }

    static Set<String> getAliases(String host) {
        return cache.containsKey(host) ? ((InstanceDiscoveryMetadataEntry)cache.get(host)).aliases() : Collections.singleton(host);
    }

    static AadInstanceDiscoveryResponse parseInstanceDiscoveryMetadata(String instanceDiscoveryJson) {
        try {
            AadInstanceDiscoveryResponse aadInstanceDiscoveryResponse = (AadInstanceDiscoveryResponse)JsonHelper.convertJsonToObject(instanceDiscoveryJson, AadInstanceDiscoveryResponse.class);
            return aadInstanceDiscoveryResponse;
        } catch (Exception var3) {
            throw new MsalClientException("Error parsing instance discovery response. Data must be in valid JSON format. For more information, see https://aka.ms/msal4j-instance-discovery", "invalid_instance_discovery_metadata");
        }
    }

    static void cacheInstanceDiscoveryMetadata(String host, AadInstanceDiscoveryResponse aadInstanceDiscoveryResponse) {
        if (aadInstanceDiscoveryResponse.metadata() != null) {
            InstanceDiscoveryMetadataEntry[] var2 = aadInstanceDiscoveryResponse.metadata();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                InstanceDiscoveryMetadataEntry entry = var2[var4];
                Iterator var6 = entry.aliases().iterator();

                while(var6.hasNext()) {
                    String alias = (String)var6.next();
                    cache.put(alias, entry);
                }
            }
        }

        cache.putIfAbsent(host, InstanceDiscoveryMetadataEntry.builder().preferredCache(host).preferredNetwork(host).build());
    }

    private static String getAuthorizeEndpoint(String host, String tenant) {
        return "https://{host}/{tenant}/oauth2/v2.0/authorize".replace("{host}", host).replace("{tenant}", tenant);
    }

    private static String getInstanceDiscoveryEndpoint(String host) {
        String discoveryHost = TRUSTED_HOSTS_SET.contains(host) ? host : AccessConfig.DEFAULT_TRUSTED_HOST;
        return "https://{host}/common/discovery/instance".replace("{host}", discoveryHost);
    }

    private static AadInstanceDiscoveryResponse sendInstanceDiscoveryRequest(URL authorityUrl, MsalRequest msalRequest, ServiceBundle serviceBundle) {
        String instanceDiscoveryRequestUrl = getInstanceDiscoveryEndpoint(authorityUrl.getAuthority()) + "?api-version=1.1&authorization_endpoint={authorizeEndpoint}".replace("{authorizeEndpoint}", getAuthorizeEndpoint(authorityUrl.getAuthority(), Authority.getTenant(authorityUrl, Authority.detectAuthorityType(authorityUrl))));
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET, instanceDiscoveryRequestUrl, msalRequest.headers().getReadonlyHeaderMap());
        IHttpResponse httpResponse = HttpHelper.executeHttpRequest(httpRequest, msalRequest.requestContext(), serviceBundle);
        return (AadInstanceDiscoveryResponse)JsonHelper.convertJsonToObject(httpResponse.body(), AadInstanceDiscoveryResponse.class);
    }

    private static void doInstanceDiscoveryAndCache(URL authorityUrl, boolean validateAuthority, MsalRequest msalRequest, ServiceBundle serviceBundle) {
        AadInstanceDiscoveryResponse aadInstanceDiscoveryResponse = sendInstanceDiscoveryRequest(authorityUrl, msalRequest, serviceBundle);
        if (validateAuthority) {
            validate(aadInstanceDiscoveryResponse);
        }

        cacheInstanceDiscoveryMetadata(authorityUrl.getAuthority(), aadInstanceDiscoveryResponse);
    }

    private static void validate(AadInstanceDiscoveryResponse aadInstanceDiscoveryResponse) {
        if (StringHelper.isBlank(aadInstanceDiscoveryResponse.tenantDiscoveryEndpoint())) {
            throw new MsalServiceException(aadInstanceDiscoveryResponse);
        }
    }

//    static {
//        TRUSTED_HOSTS_SET = new TreeSet(String.CASE_INSENSITIVE_ORDER);
//        cache = new ConcurrentHashMap();
//        TRUSTED_HOSTS_SET.addAll(Arrays.asList("login.windows.net", "login.chinacloudapi.cn", "login-us.microsoftonline.com", "login.microsoftonline.de", "login.microsoftonline.com", "login.microsoftonline.us"));
//    }
}
