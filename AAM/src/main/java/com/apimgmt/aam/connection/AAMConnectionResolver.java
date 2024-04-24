package com.apimgmt.aam.connection;


import static com.apimgmt.aam.util.AAMConstant.LOG_GATEWAY_NAME;
import static com.apimgmt.aam.util.AAMConstant.REPOSITORY_NAME;

import com.apimgmt.aam.util.ConnectionUtil;
import com.common.api.connection.IConnectivityResolver;
import com.common.exception.ConnectionResolverException;
import com.common.model.ConnectionProperties;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@Slf4j
public class AAMConnectionResolver implements IConnectivityResolver {

  public static String getToken() throws ConnectionResolverException {
    String token = "";
    try {
      String tokenUrl = AAMConnectionParameter.getTokenUrl();
      String tenantId = AAMConnectionParameter.getTenantId();
      String clientId = AAMConnectionParameter.getClientId();
      String resource = AAMConnectionParameter.getResource();
      String clientSecret = AAMConnectionParameter.getClientSecret();
      String gatewayTokenURL = String.format("%s/%s/oauth2/token", tokenUrl, tenantId);
      HttpPost post = new HttpPost(gatewayTokenURL);
      ArrayList<NameValuePair> parameters = new ArrayList<>();
      parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
      parameters.add(new BasicNameValuePair("client_id", clientId));
      parameters.add(new BasicNameValuePair("resource", resource));
      parameters.add(new BasicNameValuePair("client_secret", clientSecret));
      post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
      try (CloseableHttpClient httpClient = HttpClients.createDefault();
           CloseableHttpResponse response = httpClient.execute(post)) {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (response != null && ConnectionUtil.isSuccess(statusCode)) {
          JsonObject jobj = new Gson().fromJson(EntityUtils.toString(response.getEntity()),
            JsonObject.class);
          if (jobj.has("access_token") && jobj.get("access_token") != null) {
            token = jobj.get("access_token").getAsString();
          }
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new ConnectionResolverException("Error in fetching Token Unsupported Encoding",
        e);
    } catch (Exception e) {
      String msg = String.format("Error in fetching %s Token", LOG_GATEWAY_NAME);
      throw new ConnectionResolverException(msg, e);
    }
    log.debug("{} access token {} :", LOG_GATEWAY_NAME, token);
    if (token == null || token.isEmpty()) {
      String msg = String.format("Error in fetching %s Token", LOG_GATEWAY_NAME);
      throw new ConnectionResolverException(msg);
    }
    return token;
  }

  @Override
  public CloseableHttpClient getHttpClient(String connectionName)
    throws ConnectionResolverException {
    List<Header> defaultHeaders = new ArrayList<>();
    defaultHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
    defaultHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getToken()));
    return HttpClients.custom().setDefaultHeaders(defaultHeaders).build();
  }

  @Override
  public CloseableHttpClient getCustomHttpClient(String connectionName, ConnectionProperties custom)
    throws ConnectionResolverException {
    List<Header> defaultHeaders = new ArrayList<>();
    defaultHeaders.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
    defaultHeaders.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getToken()));
    return HttpClients.custom()
      .disableRedirectHandling()
      .setDefaultHeaders(defaultHeaders)
      .build();
  }

  @Override
  public String getType() {
    return REPOSITORY_NAME;
  }
}
