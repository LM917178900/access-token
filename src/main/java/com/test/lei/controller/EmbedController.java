package com.test.lei.controller;

import com.alibaba.fastjson.JSONObject;
import com.test.lei.config.Config;
import com.test.lei.models.EmbedConfig;
import com.test.lei.services.AzureADService;
import com.test.lei.services.PowerBIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class EmbedController {
	
	static final Logger logger = LoggerFactory.getLogger(com.test.lei.controller.EmbedController.class);

	/** 
	 * Home page controller
	 * @return Homepage (jsp)
	 */
	@GetMapping(path = "/")
	public ModelAndView embedReportHome() {
		
		// Return homepage JSP view
		return new ModelAndView("EmbedReport");
	}
	
	/** 
	 * Embedding details controller
	 * @return ResponseEntity<String> body contains the JSON object with embedUrl and embedToken 
	 */
	@GetMapping(path = "/getembedinfo")
	@ResponseBody
	public ResponseEntity<String> embedInfoController() {
	
		// Get access token
		String accessToken;
		try {
			accessToken = AzureADService.getAccessToken();
		} catch (ExecutionException | MalformedURLException | RuntimeException ex) {
			// Log error message
			logger.error(ex.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
			
		} catch (InterruptedException interruptedEx) {
			// Log error message
			logger.error(interruptedEx.getMessage());
			
			Thread.currentThread().interrupt();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(interruptedEx.getMessage());
		}
		
		// Get required values for embedding the report
		try {
			// Get report details
			EmbedConfig reportEmbedConfig = PowerBIService.getReportEmbedDetails(Config.reportId, Config.groupId, accessToken);

			// Get embed token
			reportEmbedConfig.embedToken = PowerBIService.getMultiResourceEmbedToken(reportEmbedConfig.reportId, reportEmbedConfig.datasetId, accessToken);
			
			// Return JSON response in string
			JSONObject responseObj = new JSONObject();
			responseObj.put("embedToken", reportEmbedConfig.embedToken.token);
			responseObj.put("embedUrl", reportEmbedConfig.embedUrl);
			responseObj.put("tokenExpiry", reportEmbedConfig.embedToken.expiration);
			
			String response = responseObj.toString();
			return ResponseEntity.ok(response);
			
		} catch (HttpClientErrorException hcex) {
			// Build the error message
			StringBuilder errMsgStringBuilder = new StringBuilder("Error: "); 
			errMsgStringBuilder.append(hcex.getMessage());

			// Get Request Id
			HttpHeaders header = hcex.getResponseHeaders();
			List<String> requestIds = header.get("requestId");
			if (requestIds != null) {
				for (String requestId: requestIds) {
					errMsgStringBuilder.append("\nRequest Id: ");
					errMsgStringBuilder.append(requestId);
				}
			}
			
			// Error message string to be returned
			String errMsg = errMsgStringBuilder.toString();
			
			// Log error message
			logger.error(errMsg);
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
			
		} catch (RuntimeException rex) {
			// Log error message
			logger.error(rex.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rex.getMessage());
		}
	}

	/**
	 * 从云端同步自己的报表到系统
	 *
	 * @author: leiming5
	 */
	@GetMapping("/test1")
	public String test1(String authenticationType) throws InterruptedException, ExecutionException, MalformedURLException {

		Config.authenticationType = authenticationType;
		// 代码中的token
		String token = null;
		if (authenticationType.equalsIgnoreCase("MasterUser")) {
			token =  AzureADService.getAccessTokenUsingMasterUser(Config.clientId, Config.pbiUsername, Config.pbiPassword);
		} else if (authenticationType.equalsIgnoreCase("ServicePrincipal")) {
			// Check if Tenant Id is empty
			if (Config.tenantId.isEmpty()) {
				throw new RuntimeException("Tenant Id is empty");
			}
			token = AzureADService.getAccessTokenUsingServicePrincipal(Config.clientId, Config.tenantId, Config.appSecret);
		}

		String url = "https://api.powerbi.cn/v1.0/myorg/groups/fecd78a9-73f5-49c9-ae8d-40bd3590112e/reports";
		Map<String, String> header = new HashMap<>(16);
		header.put("Authorization", "Bearer " + token);

		String s = doGet(url, header);
		return s;
	}

	@GetMapping("/test2")
	public String test2() throws InterruptedException, ExecutionException, MalformedURLException {

		// 北京互联给的api,获取token
		String token = getEmailToken();
		String url = "https://api.powerbi.cn/v1.0/myorg/groups/fecd78a9-73f5-49c9-ae8d-40bd3590112e/reports";
		Map<String, String> header = new HashMap<>(16);
		header.put("Authorization", "Bearer " + token);

		String s = doGet(url, header);
		return s;
	}

	@GetMapping("test3")
	public String test3() throws InterruptedException, ExecutionException, MalformedURLException {

		// 用户名称和密码获取的token
		String token = getRightToken();
		String url = "https://api.powerbi.cn/v1.0/myorg/groups/fecd78a9-73f5-49c9-ae8d-40bd3590112e/reports";
		Map<String, String> header = new HashMap<>(16);
		header.put("Authorization", "Bearer " + token);

		String s = doGet(url, header);
		return s;
	}
	@GetMapping("/test4")
	public String test4() throws InterruptedException, ExecutionException, MalformedURLException {

		// 代码中提取的api
		String token = getExtractToken();
		String url = "https://api.powerbi.cn/v1.0/myorg/groups/fecd78a9-73f5-49c9-ae8d-40bd3590112e/reports";
		Map<String, String> header = new HashMap<>(16);
		header.put("Authorization", "Bearer " + token);

		String s = doGet(url, header);
		return s;
	}
	private String getExtractToken(){
		String token = "";
//		String url = "https://login.chinacloudapi.cn/a6c1b34e-d17f-48de-83b8-8e248b0f0360/oauth2/token";
		String url = "https://login.partner.microsoftonline.cn/a6c1b34e-d17f-48de-83b8-8e248b0f0360/oauth2/v2.0/token";
		Map<String, String> header = new HashMap<>(16);
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept", "*/*");

		Map<String, String> body = new HashMap<>(16);
		body.put("grant_type", "client_credentials");
		body.put("scope", "https://analysis.chinacloudapi.cn/powerbi/api/.default");
		body.put("client_id", "fd76c922-a937-416e-8034-41b326899f16");
		body.put("client_secret", "qmc?O_[rE_4CvYIrLR8Sjr11s2FhKmfy");

		StringBuilder bodyStr = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : body.entrySet()) {
			i++;
			bodyStr.append(entry.getKey()).append("=").append(entry.getValue());
			if (i < body.size()) {
				bodyStr.append("&");
			}
		}

		String baseStr = doPost(url, header, bodyStr.toString());

		JSONObject jsonObject = JSONObject.parseObject(baseStr);
		token = jsonObject.getString("access_token");

		return token;
	}

	private String getEmailToken(){

		String token = "";
		String url = "https://login.chinacloudapi.cn/a6c1b34e-d17f-48de-83b8-8e248b0f0360/oauth2/token";

		Map<String, String> header = new HashMap<>(16);
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept", "*/*");

		Map<String, String> body = new HashMap<>(16);
		body.put("grant_type", "client_credentials");
		body.put("resource", "https://analysis.chinacloudapi.cn/powerbi/api");
		body.put("client_id", "fd76c922-a937-416e-8034-41b326899f16");
		body.put("client_secret", "qmc?O_[rE_4CvYIrLR8Sjr11s2FhKmfy");

		StringBuilder bodyStr = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : body.entrySet()) {
			i++;
			bodyStr.append(entry.getKey()).append("=").append(entry.getValue());
			if (i < body.size()) {
				bodyStr.append("&");
			}
		}

		String baseStr = doPost(url, header, bodyStr.toString());

		JSONObject jsonObject = JSONObject.parseObject(baseStr);
		token = jsonObject.getString("access_token");

		return token;
	}

	public String doGet(String url, Map<String, String> header) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
			connection.setRequestMethod("GET");

			// 设置 header
			for (Map.Entry<String, String> entry : header.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			// 设置请求 body
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			in = new BufferedReader(inputStreamReader);
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result.toString();
	}
	private String getRightToken(){

		return getPasswordToken();
	}

	public String getPasswordToken() {

		String token = "";
		String url = "https://login.partner.microsoftonline.cn/common/oauth2/token";

		Map<String, String> header = new HashMap<>(16);
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept", "*/*");

		Map<String, String> body = new HashMap<>(16);
		body.put("resource", "https://analysis.chinacloudapi.cn/powerbi/api");
		body.put("client_id", "fd76c922-a937-416e-8034-41b326899f16");
		body.put("client_secret", "qmc?O_[rE_4CvYIrLR8Sjr11s2FhKmfy");
		body.put("grant_type", "password");
		body.put("username", "zhaozg4@lenovocloudbroker.partner.onmschina.cn");
		body.put("password", "MBGQESpowerb1gateway");

		StringBuilder bodyStr = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> entry : body.entrySet()) {
			i++;
			bodyStr.append(entry.getKey()).append("=").append(entry.getValue());
			if (i < body.size()) {
				bodyStr.append("&");
			}
		}

		String baseStr = doPost(url, header, bodyStr.toString());

		JSONObject jsonObject = JSONObject.parseObject(baseStr);
		token = jsonObject.getString("access_token");

		return token;
	}

	public String doPost(String url, Map<String, String> header, String body) {

		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("POST");
			// 设置 header
			for (Map.Entry<String, String> entry : header.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			// 设置请求 body
			connection.setDoOutput(true);
			connection.setDoInput(true);

			//设置连接超时和读取超时时间
			connection.setConnectTimeout(20000);
			connection.setReadTimeout(20000);
			try {
				out = new PrintWriter(connection.getOutputStream());
				// 保存body
				out.print(body);
				// 发送body
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// 获取响应body
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		}
		return result;
	}
}