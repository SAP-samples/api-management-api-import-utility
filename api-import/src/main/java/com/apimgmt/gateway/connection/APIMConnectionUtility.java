package com.apimgmt.gateway.connection;

import com.apimgmt.gateway.client.OdataConnectionUtil;
import com.apimgmt.gateway.exception.APIManagementHTTPException;
import com.common.exception.FileReadException;
import com.common.util.JSONReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@Slf4j
public class APIMConnectionUtility {

  private static final String APIPORTAL_API_1_0_MANAGEMENT_SVC = "apiportal/api/1.0/Management.svc";
  private static String clientId = "";
  private static String clientSecret = "";
  private static String tokenUrl = "";
  private static String apiUrl = "";

  static {
    try {
      JsonObject connObject = JSONReader.readConfigFile("SAP_service_key.json", JsonObject.class);
      log.debug("APIM Connection obj: {} ", connObject);
      clientId = connObject.get("clientId").getAsString();
      clientSecret = connObject.get("clientSecret").getAsString();
      tokenUrl = connObject.get("tokenUrl").getAsString();
      apiUrl = connObject.get("url").getAsString();
    } catch (FileReadException e) {
      log.error("Failed to load API Management connectivity details with error {} ", e.getMessage());
    }
  }

  /**
   * Get Access Token
   * @return Access Token
   * @throws APIManagementHTTPException
   */


  public static String getAccessToken() throws APIManagementHTTPException {
    String token = "";
    HttpPost post = new HttpPost(tokenUrl);
    ArrayList<NameValuePair> parameters;
    parameters = new ArrayList<>();
    parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
    parameters.add(new BasicNameValuePair("client_id", clientId));
    parameters.add(new BasicNameValuePair("client_secret", clientSecret));
    try {
      post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new APIManagementHTTPException("Error in fetching Server OAuth Token", e);
    }
    try (CloseableHttpClient httpClient = HttpClients.createDefault();
         CloseableHttpResponse response = httpClient.execute(post)) {
      final int statusCode = response.getStatusLine().getStatusCode();
      if (response != null && OdataConnectionUtil.isSuccess(statusCode)) {
        JsonObject jobj = new Gson().fromJson(EntityUtils.toString(response.getEntity()),
                JsonObject.class);
        if (jobj.has("access_token") && jobj.get("access_token") != null) {
          token = jobj.get("access_token").getAsString();
        }
      }
    } catch (IOException e) {
      throw new APIManagementHTTPException("Error in fetching OAuth Token", e);
    }
    return token;
  }

  public static String createProductUri() {
    return String.format("%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC + "/APIProducts", apiUrl);
  }

  public static String getProductUri(String productName) {
    return String.format(
            "%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC + "/APIProducts('%s')?$expand=apiProxies", apiUrl,
            productName);
  }

  public static String createAPIProxyUri() {
    return String.format("%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC + "/APIProxies", apiUrl);
  }

  public static String getAPIProxyUri(String apiName) {
    return String.format("%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC
                    + "/APIProxies('%s')?$expand=proxyEndPoints,apiProducts,"
                    + "proxyEndPoints/apiResources,proxyEndPoints/apiResources/documentations",
            apiUrl, apiName);
  }

  public static String deleteAPIProxyUri(String apiName) {
    return String.format("%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC + "/APIProxies('%s')", apiUrl,
            apiName);
  }

  public static String getBatchUri() {
    return String.format("%s/" + APIPORTAL_API_1_0_MANAGEMENT_SVC + "/$batch", apiUrl);
  }
}
