package com.apimgmt.aam.provider;

import static com.apimgmt.aam.util.AAMConstant.LOG_GATEWAY_NAME;
import static com.apimgmt.aam.util.AAMConstant.REPOSITORY_NAME;
import static com.common.ConnectionConstants.MAX_RETRIES;


import com.apimgmt.aam.connection.AAMConnectionParameter;
import com.apimgmt.aam.exception.AAMManagementBusinessException;
import com.apimgmt.aam.transformer.AAMTransformer;
import com.apimgmt.aam.util.ConnectionUtil;
import com.common.RetryMechanism;
import com.common.SmartDiscoveryExecutor;
import com.common.api.gateway.IRepositoryService;
import com.common.exception.APIManagementExecutorException;
import com.common.exception.ConnectionResolverException;
import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import com.common.model.APIDefinition;
import com.common.model.ConnectionModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;


/*
 *
 * List of Api discovery is done via Api Manager which is Design Time
 * Api definition is from the exchange
 *
 *
 */
@Slf4j
public class AAMApiRepository implements IRepositoryService {

  private CloseableHttpClient httpClient;
  private ConnectionModel connectionModel;
  private RetryMechanism<CloseableHttpResponse> retryMechanism = new RetryMechanism<>(MAX_RETRIES);

  @Override
  public void initializeService(ConnectionModel model) throws OpenDiscoveryException {
    try {
      CloseableHttpClient client = ConnectionUtil.getHttpClient(model);
      this.httpClient = client;
      this.connectionModel = model;
    } catch (ConnectionResolverException e) {
      throw new OpenDiscoveryException(e);
    }
  }

  @Override
  public List<API> getListOfApis() throws OpenDiscoveryException {
    List<API> listOfAPI = new ArrayList<>();
    try {
      String resource = AAMConnectionParameter.getResource();
      String subscriptionId = AAMConnectionParameter.getSubscriptionId();
      String resourceGroup = AAMConnectionParameter.getResourceGroup();
      String serviceName = AAMConnectionParameter.getServiceName();
      String url = String.format(AAMConnectionParameter.getGetAPISUrl(),
        resource, subscriptionId, resourceGroup, serviceName);
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response =
        retryMechanism.run(() -> executeHttpMethod(this.httpClient, httpGet));
      final int statusCode = response.getStatusLine().getStatusCode();
      if (ConnectionUtil.isSuccess(statusCode)) {
        String data = (EntityUtils.toString(response.getEntity()));
        JsonObject jobj = new Gson().fromJson(data, JsonObject.class);
        if (jobj.has("value") && jobj.get("value") != null) {
          JsonArray apiList = jobj.get("value").getAsJsonArray();
          final List<API> apis = AAMTransformer.transformToAPI(apiList);
          listOfAPI.addAll(apis);
        }
        // Populate API Definition
        SmartDiscoveryExecutor<API> executor = new SmartDiscoveryExecutor<>("api-definition", 10);
        List<Supplier<API>> tasks = new ArrayList<>();
        listOfAPI.forEach((api) -> {
          tasks.add(() -> populateAPIDefinition(api));
        });
        final List<API> populatedApis = executor.executeTasks(tasks);
        log.info("Found {} API from {} API Management System", populatedApis.size(), LOG_GATEWAY_NAME);
        return populatedApis;
      } else {
        throw new OpenDiscoveryException("Error in Fetching APIs status code : " + statusCode);
      }
    } catch (AAMManagementBusinessException | APIManagementExecutorException e) {
      throw new OpenDiscoveryException("Error in Fetching APIs : " + e.getMessage(), e);
    } catch (IOException e) {
      throw new OpenDiscoveryException("Error in Fetching APIs : " + e.getMessage(), e);
    }
  }

  @Override
  public API getApi(String apiIdName) throws OpenDiscoveryException {
    API responseApi = null;
    try {
      String resource = AAMConnectionParameter.getResource();
      String subscriptionId = AAMConnectionParameter.getSubscriptionId();
      String resourceGroup = AAMConnectionParameter.getResourceGroup();
      String serviceName = AAMConnectionParameter.getServiceName();
      String url = String.format(AAMConnectionParameter.getGetAPIUrl(),
        resource, subscriptionId, resourceGroup, serviceName, apiIdName);
      HttpGet httpGet = new HttpGet(url);
      CloseableHttpResponse response =
        retryMechanism.run(() -> executeHttpMethod(this.httpClient, httpGet));
      final int statusCode = response.getStatusLine().getStatusCode();
      if (ConnectionUtil.isSuccess(statusCode)) {
        String data = (EntityUtils.toString(response.getEntity()));
        JsonObject apiObj = new Gson().fromJson(data, JsonObject.class).getAsJsonObject();
        responseApi = AAMTransformer.apiResponse(apiObj);
        responseApi.setApiDefinition(getApiDefinition(responseApi));
      } else {
        throw new OpenDiscoveryException("Error in Fetching API: status code  " + statusCode);
      }
    } catch (AAMManagementBusinessException | IOException e) {
      throw new OpenDiscoveryException("Error in Fetching API: " + apiIdName + e.getMessage(),
        e);
    }
    return responseApi;
  }

  @Override
  public APIDefinition getApiDefinition(API api) throws OpenDiscoveryException {
    try {
      CloseableHttpClient client = ConnectionUtil.getCustomHttpClient(this.connectionModel);
      return getApiDefinition(api, client);
    } catch (ConnectionResolverException e) {
      throw new OpenDiscoveryException("Error in fetching client during API Definition retrieval", e);
    }
  }

  private APIDefinition getApiDefinition(API api, CloseableHttpClient client)
    throws OpenDiscoveryException {
    String apiIdName = api.getApiId();
    try {
      String resource = AAMConnectionParameter.getResource();
      String subscriptionId = AAMConnectionParameter.getSubscriptionId();
      String resourceGroup = AAMConnectionParameter.getResourceGroup();
      String serviceName = AAMConnectionParameter.getServiceName();
      String url = String.format(AAMConnectionParameter.getGetAPIDefinitionUrl(),
        resource, subscriptionId, resourceGroup, serviceName, apiIdName);

      HttpGet httpGet =
        new HttpGet(url);

      try (CloseableHttpResponse response = retryMechanism.run(
        () -> executeHttpMethod(client, httpGet))) {
        if (response.getStatusLine().getStatusCode() == 404) {
          return null;
        }
        String data = (EntityUtils.toString(response.getEntity()));
        JsonObject jobj = new Gson().fromJson(data, JsonObject.class);
        JsonObject json = jobj.getAsJsonObject();
        if (json.has("value") && !json.get("value").isJsonNull()) {
          JsonObject json1 = json.get("value").getAsJsonObject();
          String contentApiInfo = new Gson().toJson(json1);
          APIDefinition apiDefinition = new APIDefinition();
          apiDefinition.setContent(contentApiInfo);
          log.info("Fetched definition for api {} ", api.getApiName());
          log.debug("API Definition for api: {}  definition :{}", api.getApiName(), apiDefinition);
          return apiDefinition;
        }
      }
    } catch (Exception e) {
      throw new OpenDiscoveryException("Error in fetching API Definition", e);
    }
    return null;
  }

  @Override
  public String getType() {
    return REPOSITORY_NAME;
  }

  private CloseableHttpResponse executeHttpMethod(CloseableHttpClient client,
                                                  HttpRequestBase httpRequest) {
    try {
      return client.execute(httpRequest);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private API populateAPIDefinition(API api) {
    try {
      CloseableHttpClient client = ConnectionUtil.getCustomHttpClient(this.connectionModel);
      api.setApiDefinition(getApiDefinition(api, client));
    } catch (OpenDiscoveryException | RuntimeException | ConnectionResolverException e) {
      log.error("Error in populating API definition for {} API : {}", LOG_GATEWAY_NAME, api.getApiName());
    }
    return api;
  }

}
