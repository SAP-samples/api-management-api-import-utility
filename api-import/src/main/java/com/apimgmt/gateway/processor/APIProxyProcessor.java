package com.apimgmt.gateway.processor;


import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.client.BatchRequest;
import com.apimgmt.gateway.client.BatchResponse;
import com.apimgmt.gateway.client.HttpMethod;
import com.apimgmt.gateway.client.ODataClient;
import com.apimgmt.gateway.client.OdataConnectionUtil;
import com.apimgmt.gateway.comparator.DiffEntity;
import com.apimgmt.gateway.comparator.EntityComparator;
import com.apimgmt.gateway.connection.APIMConnectionUtility;
import com.apimgmt.gateway.exception.APIManagementHTTPException;
import com.apimgmt.gateway.model.ResponseData;
import com.apimgmt.gateway.model.input.UnmanagedAPIProxyIM;
import com.apimgmt.gateway.model.output.APIProxyData;
import com.apimgmt.gateway.model.output.ToProxyEndPoint;
import com.apimgmt.gateway.transformer.APIMgmtTransformer;
import com.apimgmt.gateway.util.GsonProvider;
import com.apimgmt.gateway.util.LongDeserializer;
import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

@Slf4j
public class APIProxyProcessor {

  private ODataClient client;
  private ProxyEndPointProcessor proxyEndPointProcessor;

  public APIProxyProcessor() {
    this.client = new ODataClient();
    this.proxyEndPointProcessor = new ProxyEndPointProcessor();
  }

  /* Create or Update Proxy
   * @param api API
   * @param token Token
   * @return ResponseData
   * @throws CompletionException
   */
  public ResponseData createUpdateProxy(API api, String token) throws CompletionException {
    try {
      APIProxyData apiInTarget = getAPIProxy(api.getApiName(), token);
      APIProxyData apiInSource = APIMgmtTransformer.transform(api);
      if (apiInTarget == null) {
        // Create Proxy
        return createAPIProxy(apiInSource, token);
      } else {
        // updateProxy
        final List<BatchResponse> batchResponses = updateAPIProxy(apiInSource, apiInTarget);
        return OdataConnectionUtil.parseResponse(batchResponses);
      }
    } catch (OpenDiscoveryException e) {
      throw new CompletionException(e);
    }
  }


  /* Get API Proxy
   * @param proxyName Proxy Name
   * @param token Token
   * @return APIProxyData
   * @throws OpenDiscoveryException
   */
  public APIProxyData getAPIProxy(String proxyName, String token) throws OpenDiscoveryException {
    APIProxyData apiProxyData = null;
    try {
      String url = APIMConnectionUtility.getAPIProxyUri(proxyName);
      HttpResponse response = client.get(url, token);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
        return null;
      }
      JsonObject d =
        JsonParser.parseString(EntityUtils.toString(response.getEntity())).getAsJsonObject()
          .getAsJsonObject("d");
      Gson gson =
        new GsonBuilder().registerTypeAdapter(UnmanagedAPIProxyIM.class, new LongDeserializer(
            new UnmanagedAPIProxyIM()))
          .create();
      UnmanagedAPIProxyIM unmanagedAPIProxyIM = gson.fromJson(d, UnmanagedAPIProxyIM.class);
      apiProxyData = unmanagedAPIProxyIM.getPaylod();
      log.info("Fetched details for API {}", proxyName);
    } catch (APIManagementHTTPException | IOException e) {
      log.error("Error in Fetching Entity for : {} , error : {} ", proxyName, e.getMessage());
      throw new OpenDiscoveryException("Error in Fetching Entity for : " + proxyName, e);
    }
    return apiProxyData;
  }

  /* Create API Proxy
   * @param apiInSource APIProxyData
   * @param token Token
   * @return ResponseData
   * @throws OpenDiscoveryException
   */
  public ResponseData createAPIProxy(APIProxyData apiInSource, String token)
    throws OpenDiscoveryException {
    ResponseData<String> res = new ResponseData();
    try {
      Gson gson = GsonProvider.getGson();
      String url = APIMConnectionUtility.createAPIProxyUri();
      HttpResponse response = client.create(url, gson.toJson(apiInSource), token);
      final int statusCode = response.getStatusLine().getStatusCode();
      res.setStatus_code(statusCode);
      res.setSpec(apiInSource.getName());
      if (OdataConnectionUtil.isSuccess(statusCode)) {
        log.info("Created Proxy name : {}", apiInSource.getName());
        log.debug("Created Proxy name : {} and request {} ", apiInSource.getName(), apiInSource);
        res.setSuccess(true);
        return res;
      } else {
        throw new OpenDiscoveryException("Error in Proxy Creation : code " + statusCode);
      }

    } catch (APIManagementHTTPException e) {
      log.error("Error in proxy Creation : {} , response : {} ", e.getMessage(), apiInSource);
      throw new OpenDiscoveryException("Error in Proxy Creation", e);
    }
  }

  /*  Delete API Proxy
   * @param apiName API Name
   * @return ResponseData
   * @throws CompletionException
   */
  public ResponseData deleteAPIProxy(String apiName) throws CompletionException {
    ResponseData<String> res = new ResponseData();
    int statusCode = 0;
    try {
      String url = APIMConnectionUtility.deleteAPIProxyUri(apiName);
      HttpResponse response = client.delete(url);
      statusCode = response.getStatusLine().getStatusCode();
      if (OdataConnectionUtil.isSuccess(statusCode)) {
        log.info("Deleted Proxy Successfully : {}", apiName);
        res.setSuccess(true);
      } else {
        log.error("Delete Proxy {} Failed  with error code {}", apiName, statusCode);
      }
      res.setStatus_code(statusCode);
      return res;
    } catch (Exception e) {
      throw new CompletionException(e);
    }
  }

  /* Update API Proxy
   * @param src APIProxyData
   * @param dest APIProxyData
   * @return List of BatchResponse
   */
  public List<BatchResponse> updateAPIProxy(APIProxyData src, APIProxyData dest) {
    List<BatchRequest> batchRequests = new ArrayList<>();
    List<BatchResponse> batchResponses = new ArrayList<>();
    String proxyName = src.getName();
    try {
      Gson gson = GsonProvider.getGson();
      src.setPublished(true);
      // check the need for Proxy Update
      if (!src.equals(dest)) {
        String putProxyUri = String.format(BatchConstants.ADD_PROXIES, proxyName);
        batchRequests.add(new BatchRequest(putProxyUri, HttpMethod.PUT,
          gson.toJson(getProxyUpdateBatchPayload(src))));
        log.info("Added API  {} ", proxyName);
        // Check the need for Proxy Endpoint Update
        List<ToProxyEndPoint> srcProxyEndPoints = src.getProxyEndPoints();
        List<ToProxyEndPoint> destProxyEndPoints = dest.getProxyEndPoints();
        final DiffEntity diffEpEntity =
          EntityComparator.compareEntity(srcProxyEndPoints, destProxyEndPoints);
        // get change sets for proxyEndPoints (To do) - ProxyEndPoint processor
        final List<BatchRequest> proxyBatchRequests =
          proxyEndPointProcessor.update(diffEpEntity, destProxyEndPoints, proxyName);
        String url = APIMConnectionUtility.getBatchUri();
        batchRequests.addAll(proxyBatchRequests);
        batchResponses = client.batch(batchRequests, url);
        log.info(" Updated details for {}", proxyName);
      } else {
        log.info("Proxy- {} is up to date", proxyName);
      }

    } catch (OpenDiscoveryException e) {
      log.error("Failed to update proxy details for  : {}", proxyName);
    }
    return batchResponses;

  }

  /* Get Proxy Update Batch Payload
   * @param apiProxy APIProxyData
   * @return APIProxyData
   */
  private APIProxyData getProxyUpdateBatchPayload(final APIProxyData apiProxy) {
    APIProxyData proxyPayload = new APIProxyData();
    proxyPayload.setName(apiProxy.getName());
    proxyPayload.setTitle(apiProxy.getTitle());
    proxyPayload.setDescription(apiProxy.getDescription());
    proxyPayload.setVersion(apiProxy.getVersion());
    proxyPayload.setStatusCode(apiProxy.getStatusCode());
    proxyPayload.setServiceCode(apiProxy.getServiceCode());
    proxyPayload.setPublished(apiProxy.isPublished());
    proxyPayload.setReleaseStatus(apiProxy.getReleaseStatus());
    proxyPayload.setUnmanaged(apiProxy.isUnmanaged());
    proxyPayload.setReleaseMetadata(apiProxy.getReleaseMetadata());
    proxyPayload.setProviderName(apiProxy.getProviderName());
    proxyPayload.setState(apiProxy.getState());
    return proxyPayload;
  }

}
