package com.test.lei.services;

import com.test.lei.config.Config;
import com.test.lei.tokenUtil.ClientCredentialFactory;
import com.test.lei.tokenUtil.ClientCredentialParameters;
import com.test.lei.tokenUtil.ConfidentialClientApplication;
import com.test.lei.tokenUtil.IAuthenticationResult;
import com.test.lei.tokenUtil.IClientSecret;
import com.test.lei.tokenUtil.PublicClientApplication;
import com.test.lei.tokenUtil.UserNamePasswordParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service to authenticate using MSAL
 */
public class AzureADService {
	static final Logger logger = LoggerFactory.getLogger(AzureADService.class);
	
	// Prevent instantiation 
	private AzureADService () {
		throw new IllegalStateException("Authentication service class");
	}
	
	/**
	 * Acquires access token for the based on config values
	 * @return AccessToken
	 */
	public static String getAccessToken() throws MalformedURLException, InterruptedException, ExecutionException {
		
		if (Config.authenticationType.equalsIgnoreCase("MasterUser")) {

			return getAccessTokenUsingMasterUser(Config.clientId, Config.pbiUsername, Config.pbiPassword);
//			return "密码获取 access token";

		} else if (Config.authenticationType.equalsIgnoreCase("ServicePrincipal")) {
			// Check if Tenant Id is empty
			if (Config.tenantId.isEmpty()) {
				throw new RuntimeException("Tenant Id is empty");
			} 

			return getAccessTokenUsingServicePrincipal(Config.clientId, Config.tenantId, Config.appSecret);

		} else {	
			// Authentication Type is none of the above
			throw new RuntimeException("Invalid authentication type: " + Config.authenticationType);
		}
	}

	/**
	 * Acquires access token for the given clientId and app secret
	 * @param clientId
	 * @param tenantId
	 * @param appSecret
	 * @return AccessToken
	 */
	public static String getAccessTokenUsingServicePrincipal(String clientId, String tenantId, String appSecret) throws MalformedURLException, InterruptedException, ExecutionException {

		// Build Confidential Client App
		IClientSecret fromSecret = ClientCredentialFactory.createFromSecret(appSecret);
		ConfidentialClientApplication.Builder builder = ConfidentialClientApplication.builder(clientId, fromSecret);

		String s = Config.authorityUrl + tenantId;
		ConfidentialClientApplication.Builder authority = builder.authority(s);
		ConfidentialClientApplication app = authority.build();

		Set<String> singleton = Collections.singleton(Config.scopeUrl);
		ClientCredentialParameters.ClientCredentialParametersBuilder builder1 = ClientCredentialParameters.builder(singleton);
		ClientCredentialParameters clientCreds = builder1.build();

		// Acquire new AAD token
		CompletableFuture<IAuthenticationResult> iAuthResult = app.acquireToken(clientCreds);
		IAuthenticationResult result = iAuthResult.get();

		// Return access token if token is acquired successfully
		if (result != null && result.accessToken() != null && !result.accessToken().isEmpty()) {
			if (Config.DEBUG) {
				logger.info("Authenticated with Service Principal mode");
			}

			return result.accessToken();
		} else {
			logger.error("Failed to authenticate with Service Principal mode");
			return null;
		}

	}

	/**
	 * Acquires access token for the given clientId and user credentials
	 * @param clientId
	 * @param username
	 * @param password
	 * @return AccessToken
	 */
	public static String getAccessTokenUsingMasterUser(String clientId, String username, String password) throws MalformedURLException, InterruptedException, ExecutionException {

		// Build Public Client App
		// 获取默认地址（com）
		PublicClientApplication.Builder builder = PublicClientApplication.builder(clientId);
		String s = Config.authorityUrl + "organizations";
		// 替换默认地址(.cn)
		PublicClientApplication.Builder authority = builder.authority(s);// Use authorityUrl+tenantId if this doesn't work

		PublicClientApplication app = authority.build();


		Set<String> singleton = Collections.singleton(Config.scopeUrl);
		UserNamePasswordParameters.UserNamePasswordParametersBuilder builder1 = UserNamePasswordParameters.builder(
				singleton,
				username,
				password.toCharArray());

		UserNamePasswordParameters userCreds = builder1.build();

		// Acquire new AAD token
		CompletableFuture<IAuthenticationResult> iAuthRsult = app.acquireToken(userCreds);
		IAuthenticationResult result = iAuthRsult.get();

		// Return access token if token is acquired successfully
		if (result != null && result.accessToken() != null && !result.accessToken().isEmpty()) {
			if (Config.DEBUG) {
				logger.info("Authenticated with MasterUser mode");
			}

			return result.accessToken();
		} else {
			logger.error("Failed to authenticate with MasterUser mode");
			return null;
		}

	}
}