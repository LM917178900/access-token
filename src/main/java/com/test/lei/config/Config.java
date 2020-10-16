package com.test.lei.config;

/**
 * Configuration class
 */
public abstract class Config {

	// Set this to true, to show debug statements in console
	public static final boolean DEBUG = false;

	//	Two possible Authentication methods: 
	//	- For authentication with master user credential choose MasterUser as AuthenticationType.
	//	- For authentication with app secret choose ServicePrincipal as AuthenticationType.
	//	More details here: https://aka.ms/EmbedServicePrincipal
	// MasterUser or ServicePrincipal
	public static String authenticationType = "ServicePrincipal";

	//	Common configuration properties for both authentication types
	// Enter workspaceId / groupId
	public static final String groupId = "fecd78a9-73f5-49c9-ae8d-40bd3590112e";

	// The id of the report to embed.
	public static final String reportId = "f78308b6-fa5a-413d-b7e1-08dffecd1e74";

	// Enter Application Id / Client Id
	public static final String clientId = "fd76c922-a937-416e-8034-41b326899f16";

	// Enter MasterUser credentials
	public static final String pbiUsername = "zhaozg4@lenovocloudbroker.partner.onmschina.cn";
	public static final String pbiPassword = "MBGQESpowerb1gateway";

	// Enter ServicePrincipal credentials
	public static final String tenantId = "a6c1b34e-d17f-48de-83b8-8e248b0f0360";
	public static final String appSecret = "qmc?O_[rE_4CvYIrLR8Sjr11s2FhKmfy";

	//	DO NOT CHANGE
//	public static final String authorityUrl = "https://login.microsoftonline.com/";
//	public static final String scopeUrl = "https://analysis.windows.net/powerbi/api/.default";
	public static final String authorityUrl = "https://login.partner.microsoftonline.cn/";
	public static final String scopeUrl = "https://analysis.chinacloudapi.cn/powerbi/api/.default";

	// powerbiapp
	public String appId="fd76c922-a937-416e-8034-41b326899f16";
	public String objId="be087fe9-6e3b-4b9b-aadc-efeb352de315";

	private Config(){
		//Private Constructor will prevent the instantiation of this class directly
		throw new IllegalStateException("Config class");
	}

}