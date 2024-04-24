package com.apimgmt.gateway.processor;


import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.client.BatchRequest;
import com.apimgmt.gateway.client.BatchResponse;
import com.apimgmt.gateway.client.HttpMethod;
import com.apimgmt.gateway.client.ODataClient;
import com.apimgmt.gateway.client.OdataConnectionUtil;
import com.apimgmt.gateway.comparator.ProductEntityComparator;
import com.apimgmt.gateway.comparator.ProductEntityDiff;
import com.apimgmt.gateway.connection.APIMConnectionUtility;
import com.apimgmt.gateway.exception.APIManagementHTTPException;
import com.apimgmt.gateway.model.ProductCreateRequest;
import com.apimgmt.gateway.model.ResponseData;
import com.apimgmt.gateway.model.input.APIProductIM;
import com.apimgmt.gateway.model.output.APIProductOM;
import com.apimgmt.gateway.model.output.ProductData;
import com.apimgmt.gateway.model.output.URIMetadata;
import com.apimgmt.gateway.transformer.APIMgmtTransformer;
import com.apimgmt.gateway.util.GsonProvider;
import com.apimgmt.gateway.util.LongDeserializer;
import com.common.SmartDiscoveryExecutor;
import com.common.exception.APIManagementExecutorException;
import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;


@Slf4j
public class APIProductProcessor {
  private ODataClient client;
  private APIProxyProcessor apiProxyProcessor;
  private int threadSize = 5;

  public APIProductProcessor() {
    this.client = new ODataClient();
    this.apiProxyProcessor = new APIProxyProcessor();
  }

  public APIProductOM getProduct(String productName, String token) throws OpenDiscoveryException {
    APIProductOM productData = null;
    try {
      String url = APIMConnectionUtility.getProductUri(productName);
      HttpResponse response = client.get(url, token);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
        return null;
      }
      JsonObject d =
              JsonParser.parseString(EntityUtils.toString(response.getEntity())).getAsJsonObject()
                      .getAsJsonObject("d");
      Gson gson = new GsonBuilder().registerTypeAdapter(APIProductIM.class, new LongDeserializer(
                      new APIProductIM()))
              .create();
      APIProductIM apiProduct = gson.fromJson(d, APIProductIM.class);
      productData = apiProduct.getPaylod();
      log.info("Product details for  : {}", productData.getName());
    } catch (APIManagementHTTPException | IOException e) {
      log.error("Error in fetching product {}: with error {} ", productData, e.getMessage());
      throw new OpenDiscoveryException("Error in fetching entity for : " + productName, e);
    }
    return productData;
  }

  public ResponseData createProduct(List<String> proxies, ProductCreateRequest req, String token) {
    ResponseData<String> res = new ResponseData();
    res.setStatus_code(Integer.valueOf(HttpStatus.SC_BAD_REQUEST));
    try {
      String systemName = req.getRepositoryName();
      String url = APIMConnectionUtility.createProductUri();
      Gson requestGson = GsonProvider.getGson();
      ProductData product = APIMgmtTransformer.getFullProductData(proxies, req);
      HttpResponse response = client.create(url, requestGson.toJson(product), token);
      int statusCode = response.getStatusLine().getStatusCode();
      if (OdataConnectionUtil.isSuccess(statusCode)) {
        log.info("Product {} for {} was created successfully"
                , product.getName(), systemName);
        res.setSpec(req.getProductName());
        res.setStatus_code(statusCode);
      } else {
        log.info("Product {} for {} creation failed with status code {} "
                , product.getName(), systemName, statusCode);
      }
    } catch (APIManagementHTTPException e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  public ResponseData createUpdateProduct(ProductCreateRequest req, List<API> apis, String token)
          throws OpenDiscoveryException {
    try {
      APIProductOM dstProductData = getProduct(req.getProductName(), token);
      Gson gson = GsonProvider.getGson();
      if (dstProductData != null) {
        APIProductOM srcProductData = APIMgmtTransformer.getFullProductOM(apis, req);
        List<String> sourceEntities = srcProductData.getApiProxies();
        List<String> targetEntities = dstProductData.getApiProxies();
        List<BatchRequest> batchRequests = new ArrayList<>();
        // Update Product
        ProductData productData = APIMgmtTransformer.getProductData(req);
        final String productName = productData.getName();
        String productUri = String.format(BatchConstants.ADD_PRODUCTS, productName);
        batchRequests.add(new BatchRequest(productUri, HttpMethod.PUT, gson.toJson(productData)));
        ProductEntityDiff productDiff =
                ProductEntityComparator.compareEntity(sourceEntities, targetEntities);
        // get DeleteLink Batchrequests
        final List<BatchRequest> deleteLinks = getDeleteLinks(productName, productDiff);
        // get addlinkbatchRequests
        final List<BatchRequest> createLinks = getCreateLinks(productUri, productName, productDiff);
        log.info("Started update of proxy details for product : {}", productName);
        batchRequests.addAll(deleteLinks);
        batchRequests.addAll(createLinks);
        String url = APIMConnectionUtility.getBatchUri();
        List<BatchResponse> batchResponses = client.batch(batchRequests, url);
        final ResponseData responseData = OdataConnectionUtil.parseResponse(batchResponses);
        log.info("Updated details for product {}", productName);
        deleteProxy(productDiff);
        return responseData;
      } else {
        List<String> proxies = apis.stream().map(API::getApiName).collect(Collectors.toList());
        return createProduct(proxies, req, token);
      }
    } catch (OpenDiscoveryException e) {
      log.trace("Product update failed for product : {} ", req, e);
      log.error("Product update failed for product : {} with error {} ", req, e.getMessage());
      throw new OpenDiscoveryException("Error in product creation : ", e);
    }
  }

  private void deleteProxy(ProductEntityDiff productDiff) {
    try {
      if (productDiff != null && !productDiff.getApisToDelete().isEmpty()) {
        // Delete Proxy
        SmartDiscoveryExecutor<ResponseData> executor =
                new SmartDiscoveryExecutor<>("delete-api-", threadSize);
        List<Supplier<ResponseData>> tasks = new ArrayList<>();
        productDiff.getApisToDelete().forEach((api) -> {
          tasks.add(() -> apiProxyProcessor.deleteAPIProxy(api));
        });
        final List<ResponseData> taskResponse = executor.executeTasks(tasks);
        log.debug("API delete response {}", taskResponse);
      }
    } catch (APIManagementExecutorException e) {
      log.error("Error in deleting entity  : {} ", e.getMessage());
    }
  }

  private List<BatchRequest> getCreateLinks(String productUri, String productName,
                                            ProductEntityDiff productDiff) {
    List<BatchRequest> batchRequests = new ArrayList<>();
    Gson gson = GsonProvider.getGson();
    for (String proxy : productDiff.getApisToCreate()) {
      // create Product to Proxy Link
      String productProxyLinkUri = String.format(BatchConstants.PRODUCT_PROXY_LINK, productName);
      URIMetadata proxyUriMetadata = new URIMetadata();
      String proxyUri = String.format(BatchConstants.API_PROXIES, proxy);
      proxyUriMetadata.setUri(proxyUri);
      batchRequests.add(
              new BatchRequest(productProxyLinkUri, HttpMethod.POST, gson.toJson(proxyUriMetadata)));
      // Create Proxy to Product Link
      String proxyProductLinkUri = String.format(BatchConstants.PROXY_PRODUCT_LINK, proxy);
      URIMetadata productUriMetadata = new URIMetadata();
      productUriMetadata.setUri(productUri);
      batchRequests.add(
              new BatchRequest(proxyProductLinkUri, HttpMethod.POST, gson.toJson(productUriMetadata)));
    }
    return batchRequests;
  }

  private List<BatchRequest> getDeleteLinks(String productName, ProductEntityDiff productDiff) {
    List<BatchRequest> batchRequests = new ArrayList<>();
    for (String proxy : productDiff.getApisToDelete()) {
      String productProxyLinkUri = String.format(BatchConstants.ADD_PRODUCTS, productName)
              + BatchConstants.LINKS
              + BatchConstants.API_PROXY + String.format(BatchConstants.ADD_PARAMETER, proxy);
      String proxyProductLinkUri =
              String.format(BatchConstants.ADD_PROXIES, proxy) + BatchConstants.LINKS
                      + BatchConstants.API_PRODUCT
                      + String.format(BatchConstants.ADD_PARAMETER, productName);
      batchRequests.add(new BatchRequest(productProxyLinkUri, HttpMethod.DELETE, null));
      batchRequests.add(new BatchRequest(proxyProductLinkUri, HttpMethod.DELETE, null));
    }
    return batchRequests;
  }
}
