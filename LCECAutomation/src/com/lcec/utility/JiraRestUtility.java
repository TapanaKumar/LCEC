package com.lcec.utility;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JiraRestUtility extends Reporter {

	public static String JiraUsername = "tapana.kumar";
	public static String JiraPassword = "Lrn@1234";
	public static String JiraSearchUrl = "https://enable.lrn.com//rest//zapi//latest//zql//executeSearch?zqlQuery=issue=";
	public static String JiraExecutionUrl = "https://enable.lrn.com//rest//zapi//latest//execution//";
	public static String TestCycleName = "LCECAutomation_SanityCycle";

	// static Logger logger = Logger.getLogger(ExcelDriver.class);
	static String testCaseID;
	static String scheduleID, statusFlag;
	static int status = 0;

	public static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {

			// logger.error("Failed to bypasss certificate validation" +
			// e.getMessage());
			log("Failed to bypasss certificate validation" + e.getMessage(), true);
		}
	}

	public static String getScheduledID(String TestCaseID) throws AuthenticationException, JSONException {

		String testCycleName = "LCECAutomation_SanityCycle"; // Reading Test Cycle name
													// from config file
		String ScheduleID = null;
		ClientResponse response = null;
		Client client = null;
		try {
			String searchUrL = JiraSearchUrl;
			String credential = new String(org.apache.commons.codec.binary.Base64.encodeBase64(
					org.apache.commons.codec.binary.StringUtils.getBytesUtf8(JiraUsername + ":" + JiraPassword)));

			client = Client.create();
			disableCertificateValidation(); // disabling the certificate
											// validation
			WebResource webResource = client
					.resource(searchUrL + TestCaseID + "&cycleName=" + testCycleName.replace(" ", "-"));
			response = webResource.header("Authorization", "Basic " + credential).type("application/json")
					.accept("application/json").get(ClientResponse.class); // GET
																			// request
																			// call
			int statusCode = response.getStatus();
			if (statusCode == 401) {
				throw new AuthenticationException("Invalid Username or Password");
			}

			/*
			 * JSONArray jsonArray = new
			 * JSONArray(response.getEntity(String.class)); for(int i =0; i<
			 * jsonArray.length(); i++){ if(jsonArray.get(i) instanceof
			 * JSONObject){ JSONObject jsnObj = (JSONObject)jsonArray.get(i);
			 * String ScheduleID = (String)jsnObj.get("id"); }else i++; }
			 */

			String data = response.getEntity(String.class);
			JSONObject jsonObject = new JSONObject(data);

			JSONArray dataarray = jsonObject.getJSONArray("executions");
			for (int i = 0; i < dataarray.length();) {
				JSONObject proj = dataarray.getJSONObject(i);
				if (proj.getString("cycleName").equalsIgnoreCase(testCycleName)) {
					ScheduleID = proj.getString("id"); // reading Scheduled ID
														// based on Test cycle
														// for uniqueness
					System.out.println(ScheduleID);
					return ScheduleID;
				} else
					i++;
			}
		} catch (Exception e) {
			// logger.error("Error occurred in GET resquest"+e.getMessage());
			log("Error occurred in GET resquest" + e.getMessage(), true);
		} finally {
			if (response != null) {
				response.close();
			}
			if (client != null) {
				client.destroy();
			}
		}
		return ScheduleID;
	}

	public static String updateExecutionStatus(String ScheduleID, String statusFlag) throws AuthenticationException {
		String executionStatus = null;
		ClientResponse response = null;
		Client client = null;
		try {
			String payload = "{\"status\": \"" + statusFlag + "\"}"; // creating
																		// Payload
																		// for
																		// PUT
																		// request
			String credential = new String(org.apache.commons.codec.binary.Base64.encodeBase64(
					org.apache.commons.codec.binary.StringUtils.getBytesUtf8(JiraUsername + ":" + JiraPassword)));
			client = Client.create();
			disableCertificateValidation();
			WebResource webResource = client.resource(JiraExecutionUrl + ScheduleID + "/execute");
			response = webResource.header("Authorization", "Basic " + credential).type("application/json")
					.accept("application/json").put(ClientResponse.class, payload); // PUT
																					// request
																					// call
			int statusCode = response.getStatus();
			if (statusCode == 401) {
				throw new AuthenticationException("Invalid Username or Password");
			}
			executionStatus = response.getEntity(String.class);
		} catch (Exception e) {
			// logger.error("Error occurred in PUT resquest"+e.getMessage());
			log("Error occurred in PUT resquest" + e.getMessage(), true);
		} finally {
			if (response != null) {
				response.close();
			}
			if (client != null) {
				client.destroy();
			}
		}
		return executionStatus;
	}

	public static void updateTestStatusInJira(ITestResult result, Method method) {
		try {
			System.out.println("Updating in jira method-------------------");
			Test test = method.getAnnotation(Test.class);
			testCaseID = test.testName();
			status = result.getStatus();
			System.out.println("status:" + status);
			// Integer.toString(status);
			statusFlag = String.valueOf(status);

			scheduleID = getScheduledID(testCaseID);
			updateExecutionStatus(scheduleID, statusFlag);

			// logger.info(testCaseID+"is successfully updated
			// with"+statusFlag);
			log(testCaseID + "is successfully updated with" + statusFlag, true);

		} catch (AuthenticationException e) {
			// logger.error("Error occurred while updating testStatus of
			// "+testCaseID+ "in Jira");
			log("Error occurred while updating testStatus of " + testCaseID + "in Jira");

		} catch (JSONException e) {
			// logger.error("Error occurred while updating testStatus of
			// "+testCaseID+ "in Jira");
			log("Error occurred while updating testStatus of " + testCaseID + "in Jira");
		}
	}

}
