package com.apimgmt.aam.connection;


import static com.apimgmt.aam.util.AAMConstant.LOG_GATEWAY_NAME;

import com.common.exception.FileReadException;
import com.common.util.JSONReader;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AAMConnectionParameter {

  //Default values
  private static String clientId = "config.getClientId()";
  private static String clientSecret = "config.getClientSecret()";
  private static String tokenUrl = " config.tokenUrl()";
  private static String tenantId = " config.tenantId()";
  private static String resource = " config.resource()";
  private static String subscriptionId = " config.subscriptionId()";
  private static String resourceGroup = " config.resourceGroup()";
  private static String serviceName = " config.serviceName()";
  private static String getAPISUrl = "";
  private static String getAPIUrl = "";
  private static String getAPIDefinitionUrl = "";

  static {
    try {
      JsonObject connObject = JSONReader.readConfigFile("AAM_connection_details.json", JsonObject.class);
      JsonObject apiObject = JSONReader.readConfigFile("AAM_api_details.json", JsonObject.class);
      log.debug("{} Connection obj: {} ", LOG_GATEWAY_NAME, connObject);
      clientId = connObject.get("appId").getAsString();
      clientSecret = connObject.get("password").getAsString();
      tokenUrl = connObject.get("tokenUrl").getAsString();
      tenantId = connObject.get("tenant").getAsString();
      resource = connObject.get("resource").getAsString();
      subscriptionId = connObject.get("subscriptionId").getAsString();
      resourceGroup = connObject.get("resourceGroup").getAsString();
      serviceName = connObject.get("serviceName").getAsString();
      getAPISUrl = apiObject.get("APIsUrl").getAsString();
      getAPIUrl = apiObject.get("APIUrl").getAsString();
      getAPIDefinitionUrl = apiObject.get("APIDefinitionUrl").getAsString();
    } catch (FileReadException e) {
      log.error("Failed to load {} connectivity details error {} ", LOG_GATEWAY_NAME, e.getMessage());
    }
  }

  public static String getClientId() {
    return clientId;
  }

  public static String getClientSecret() {
    return clientSecret;
  }

  public static String getTokenUrl() {
    return tokenUrl;
  }

  public static String getTenantId() {
    return tenantId;
  }

  public static String getResource() {
    return resource;
  }

  public static String getSubscriptionId() {
    return subscriptionId;
  }

  public static String getResourceGroup() {
    return resourceGroup;
  }

  public static String getServiceName() {
    return serviceName;
  }

  public static String getGetAPISUrl() {
    return getAPISUrl;
  }

  public static String getGetAPIUrl() {
    return getAPIUrl;
  }

  public static String getGetAPIDefinitionUrl() {
    return getAPIDefinitionUrl;
  }
}
