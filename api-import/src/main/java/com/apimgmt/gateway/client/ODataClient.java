package com.apimgmt.gateway.client;

import static com.apimgmt.gateway.client.BatchConstants.MAX_RETRIES;

import com.apimgmt.gateway.connection.APIMConnectionUtility;
import com.apimgmt.gateway.exception.APIManagementHTTPException;
import com.common.RetryMechanism;
import com.common.exception.OpenDiscoveryException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSetPart;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.ep.EntityProvider;

/** This class is responsible for handling the OData client operations like create, delete, get and batch.
 It uses the RetryMechanism class to retry the operation in case of failure.
 It uses the OdataConnectionUtil class to execute the HTTP method.
 It also handles the batch operations like creating a batch request, parsing the batch response, etc.
 The isSuccess method checks if the status code is in the success range.
 The create method sends a POST request to the provided endpoint with the given body and token.
 The delete method sends a DELETE request to the provided endpoint.
 The get method sends a GET request to the provided endpoint with the given token.
 The batch method sends a batch request to the provided endpoint with the list of batch requests.
 The getHttpPost method creates a HttpPost request with the batch requests.
 The getHttpMethod method returns the appropriate HTTP method based on the input.
 The getBatchRequest method creates a batch request with the list of batch requests.
 The parseBatchResponse method parses the batch response and returns a list of BatchResponse objects.
 */



@Slf4j
public final class ODataClient {

  private static final String BOUNDARY = "batch_1";
  private static final int HTTP_CONNECTION_TIMEOUT = 55000;
  private static final int HTTP_SO_TIMEOUT = 55000;


  /**
   * Handles the read operation to the provided endpoint, in case of error, it
   * would parse the odata error and return the api management service
   * exception
   */

  public HttpResponse create(final String url, final String body, final String token)
          throws APIManagementHTTPException {
    HttpResponse response = null;
    try {
      log.debug("Create request for url {} and body {}", url, body);
      RetryMechanism<HttpResponse> retryMechanism = new RetryMechanism<>(MAX_RETRIES);
      HttpPost httpPost = (HttpPost) getHttpMethod(HttpMethod.POST, url);
      httpPost.setHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderConstants.APPLICATION_JSON);
      httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
      RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_CONNECTION_TIMEOUT).
              setSocketTimeout(HTTP_SO_TIMEOUT).build();
      httpPost.setConfig(config);
      StringEntity params = new StringEntity(body);
      httpPost.setEntity(params);
      response = retryMechanism.run(() -> OdataConnectionUtil.executeHttpMethod(httpPost));
      int status = response.getStatusLine().getStatusCode();
      if (!isSuccess(status)) {
        throw new APIManagementHTTPException("Error in create entity : " + status);
      }
    } catch (Exception e) {
      throw new APIManagementHTTPException("Error in create entity : ", e);
    }

    return response;
  }

  public HttpResponse delete(final String url) throws APIManagementHTTPException {
    HttpResponse response = null;
    try {
      log.debug("delete Request for url {}", url);
      RetryMechanism<HttpResponse> retryMechanism = new RetryMechanism<>(MAX_RETRIES);
      HttpDelete httpDelete = (HttpDelete) getHttpMethod(HttpMethod.DELETE, url);
      httpDelete.setHeader(HttpHeaders.ACCEPT,
              HttpHeaderConstants.APPLICATION_JSON);
      String token = APIMConnectionUtility.getAccessToken();
      httpDelete.setHeader("Authorization", "Bearer " + token);
      RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_CONNECTION_TIMEOUT).
              setSocketTimeout(HTTP_SO_TIMEOUT).build();
      httpDelete.setConfig(config);
      response = retryMechanism.run(() -> OdataConnectionUtil.executeHttpMethod(httpDelete));
    } catch (RuntimeException e) {
      throw new APIManagementHTTPException(e.getMessage());
    }
    int status = response.getStatusLine().getStatusCode();
    if (!isSuccess(status)) {
      throw new APIManagementHTTPException("Error in Deleting Entities : " + status);
    }
    return response;
  }

  public HttpResponse get(final String url, String token) throws APIManagementHTTPException {
    HttpResponse response = null;
    try {
      log.debug("Get request for url {}", url);
      RetryMechanism<HttpResponse> retryMechanism = new RetryMechanism<>(MAX_RETRIES);
      HttpGet httpGet = (HttpGet) getHttpMethod(HttpMethod.GET, url);
      httpGet.setHeader(HttpHeaders.ACCEPT,
              HttpHeaderConstants.APPLICATION_JSON);
      httpGet.setHeader("Authorization", "Bearer " + token);
      RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_CONNECTION_TIMEOUT).
              setSocketTimeout(HTTP_SO_TIMEOUT).build();
      httpGet.setConfig(config);
      response = retryMechanism.run(() -> OdataConnectionUtil.executeHttpMethod(httpGet));
    } catch (RuntimeException e) {
      throw new APIManagementHTTPException("Error in fetching Entities : ", e);
    }
    return response;
  }

  public List<BatchResponse> batch(final List<BatchRequest> batchRequests,
                                   final String url) throws OpenDiscoveryException {
    log.debug("Batch request {}", batchRequests);
    HttpResponse response = null;
    List<BatchResponse> batchResponses = null;
    if (batchRequests != null && batchRequests.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      RetryMechanism<HttpResponse> retryMechanism = new RetryMechanism<>(MAX_RETRIES);
      HttpRequestBase httpPost = getHttpPost(batchRequests, url);
      RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_CONNECTION_TIMEOUT).
              setSocketTimeout(HTTP_SO_TIMEOUT).build();
      httpPost.setConfig(config);
      response = retryMechanism.run(() -> OdataConnectionUtil.executeHttpMethod(httpPost));
      batchResponses = parseBatchResponse(response);
    } catch (URISyntaxException | IOException | RuntimeException | APIManagementHTTPException e) {
      throw new OpenDiscoveryException("Error in batch call : ", e);
    }
    return batchResponses;
  }

  private HttpRequestBase getHttpPost(final List<BatchRequest> batchRequests, final String uri)
          throws URISyntaxException, IOException, APIManagementHTTPException {
    HttpPost httpMethod = (HttpPost) getHttpMethod(HttpMethod.POST, uri);
    InputStream payload = getBatchRequest(batchRequests);
    ByteArrayEntity entity = new ByteArrayEntity(
            StreamUtility.toByteArray(payload));
    httpMethod.setHeader(HttpHeaders.CONTENT_TYPE,
            HttpHeaderConstants.APPLICATION_MULTIPART + BOUNDARY);
    httpMethod.setHeader(HttpHeaders.ACCEPT,
            HttpHeaderConstants.APPLICATION_JSON);
    String token = APIMConnectionUtility.getAccessToken();
    httpMethod.setHeader("Authorization", "Bearer " + token);
    httpMethod.setEntity(entity);
    return httpMethod;
  }

  private HttpRequestBase getHttpMethod(final HttpMethod method, final String uri) {
    HttpRequestBase httpMethod = null;
    switch (method) {
      case POST:
        httpMethod = new HttpPost(uri);
        break;
      case PUT:
        httpMethod = new HttpPut(uri);
        break;
      case DELETE:
        httpMethod = new HttpDelete(uri);
        break;
      case GET:
        httpMethod = new HttpGet(uri);
        break;
      default:
    }
    return httpMethod;
  }


  private InputStream getBatchRequest(final List<BatchRequest> batchRequests) {
    List<BatchPart> batchParts = new ArrayList<>();
    BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
    for (BatchRequest request : batchRequests) {
      Map<String, String> changeSetHeaders = new HashMap<String, String>();
      changeSetHeaders.put(HttpHeaders.CONTENT_TYPE,
              HttpHeaderConstants.APPLICATION_JSON);
      changeSetHeaders.put(HttpHeaders.ACCEPT,
              HttpHeaderConstants.APPLICATION_JSON);
      BatchChangeSetPart changeRequest = BatchChangeSetPart
              .method(request.getMethod().toString())
              .uri(request.getUri()).headers(changeSetHeaders)
              .body(request.getBody()).build();
      changeSet.add(changeRequest);
    }
    batchParts.add(changeSet);

    InputStream payload = EntityProvider.writeBatchRequest(batchParts,
            BOUNDARY);
    return payload;
  }

  private List<BatchResponse> parseBatchResponse(
          final HttpResponse httpResponse) throws IOException {
    List<BatchResponse> batchResponses = new ArrayList<>();
    if (httpResponse == null) {
      return batchResponses;
    }
    try {
      int status = httpResponse.getStatusLine().getStatusCode();
      Header contenTypeHeader = httpResponse.getFirstHeader(HttpHeaders.CONTENT_TYPE);
      String contentType = "";
      if (contenTypeHeader != null) {
        contentType = contenTypeHeader.getValue();
      }
      log.info("Batch update status with code {}", status);

      if (isSuccess(status)) {
        List<BatchSingleResponse> responses = EntityProvider
                .parseBatchResponse(httpResponse.getEntity()
                        .getContent(), contentType);
        for (BatchSingleResponse response : responses) {
          BatchResponse batchResponse = new BatchResponse();
          batchResponse.setStatus_code(response.getStatusCode());
          batchResponse.setStatus_info(response.getStatusInfo());
          batchResponse.setResponse(response.getBody());
          batchResponses.add(batchResponse);
        }
      }
    } catch (BatchException | IllegalStateException e) {
      e.printStackTrace();
    }
    return batchResponses;
  }

  private Boolean isSuccess(final int statusCode) {
    return statusCode >= BatchConstants.SUCCESS_MIN
            && statusCode <= BatchConstants.SUCESS_MAX;
  }

}
