package com.apimgmt.gateway.transformer;

import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.model.ProductCreateRequest;
import com.apimgmt.gateway.model.metadata.LinkMetadata;
import com.apimgmt.gateway.model.output.APIProductOM;
import com.apimgmt.gateway.model.output.APIProxyData;
import com.apimgmt.gateway.model.output.Metadata;
import com.apimgmt.gateway.model.output.ProductData;
import com.apimgmt.gateway.model.output.ToAPIResourceDocumentation;
import com.apimgmt.gateway.model.output.ToAPIResources;
import com.apimgmt.gateway.model.output.ToProxyEndPoint;
import com.apimgmt.gateway.model.output.URIMetadata;
import com.apimgmt.gateway.openapi.OASService;
import com.apimgmt.gateway.openapi.OASServiceFactory;
import com.apimgmt.gateway.openapi.OASVersion;
import com.apimgmt.gateway.openapi.OpenApiConstants;
import com.apimgmt.gateway.util.TransformerUtil;
import com.common.model.API;
import com.common.model.APIDefinition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class APIMgmtTransformer {

  /** Transform API to APIProxyData
   * @param api API
   * @return APIProxyData
   */
  public static APIProxyData transform(API api) {
    List<ToAPIResourceDocumentation> resourcesDoc = getToAPIResourceDocumentations(api);
    //get API Description
    String description = getDescriptionFromOAS(resourcesDoc);
    description = description != null ? description : api.getDescription();
    log.debug("Api {} description is {}", api.getApiName(), description);
    List<ToAPIResources> apiResources = getToAPIResources(resourcesDoc);
    List<ToProxyEndPoint> endPointsObj = getToProxyEndPoints(api, apiResources);
    APIProxyData newApiProxyEntity = new APIProxyData();
    newApiProxyEntity.setName(api.getApiName());
    newApiProxyEntity.setVersion(api.getVersion());
    newApiProxyEntity.setServiceCode(api.getProtocol());
    newApiProxyEntity.setDescription(description);
    newApiProxyEntity.setUnmanaged(true);
    newApiProxyEntity.setPublished(false);
    newApiProxyEntity.setState("EXTERNAL");
    newApiProxyEntity.setReleaseStatus("Active");
    newApiProxyEntity.setProviderName("NONE");
    newApiProxyEntity.setStatusCode("REGISTERED");
    newApiProxyEntity.setReleaseMetadata("{\"reason\":\"\"}");
    newApiProxyEntity.setTitle(api.getApiName());
    newApiProxyEntity.setProxyEndPoints(endPointsObj);
    Metadata metadata = new Metadata();
    metadata.setType("apiportal.APIProxy");
    newApiProxyEntity.setMetadata(metadata);
    return newApiProxyEntity;
  }


  public static ProductData getFullProductData(List<String> apis, ProductCreateRequest req) {
    List<LinkMetadata> proxies = new ArrayList<>();
    for (String apiName : apis) {
      LinkMetadata proxy = new LinkMetadata();
      URIMetadata data = new URIMetadata();
      String uriName = String.format(BatchConstants.ADD_PROXIES, apiName);
      data.setUri(uriName);
      proxy.setMetadata(data);
      proxies.add(proxy);
    }
    ProductData product = getProductData(req);
    product.setToAPIProxies(proxies);
    return product;

  }

  public static ProductData getProductData(ProductCreateRequest req) {
    ProductData product = new ProductData();
    product.setName(req.getProductName());
    product.setTitle(req.getProductTitle());
    product.setDescription(req.getProductDescription());
    product.setShortText(req.getProductShortText());
    product.setStatusCode("PUBLISHED");
    product.setVersion("1.0");
    Metadata metadata = new Metadata();
    metadata.setType("apiportal.APIProduct");
    product.setMetadata(metadata);
    return product;
  }

  public static APIProductOM getFullProductOM(List<API> apis, ProductCreateRequest req) {
    List<String> apiProxies = apis.stream().map(API::getApiName).collect(Collectors.toList());
    APIProductOM apiProductOM = APIProductOM.builder()
            .name(req.getProductName())
            .title(req.getProductTitle())
            .description(req.getProductDescription())
            .statusCode("PUBLISHED")
            .version("1.0")
            .apiProxies(apiProxies)
            .build();
    return apiProductOM;

  }

  private static List<ToProxyEndPoint> getToProxyEndPoints(API api,
                                                           List<ToAPIResources> apiResources) {

    if (api != null && api.getEntryPoint() != null) {
      List<ToProxyEndPoint> proxyEndPointObjs = new ArrayList<>();
      ToProxyEndPoint endPoint = new ToProxyEndPoint();
      endPoint.setPublishUrl(api.getEntryPoint());
      endPoint.setDefault(true);
      endPoint.setName("default");
      endPoint.setApiResources(apiResources);
      proxyEndPointObjs.add(endPoint);
      return proxyEndPointObjs;
    }
    return Collections.emptyList();
  }

  private static List<ToAPIResourceDocumentation> getToAPIResourceDocumentations(API api) {
    APIDefinition apiDefinition = api.getApiDefinition();
    if (apiDefinition != null) {
      String data = apiDefinition.getContent();
      List<ToAPIResourceDocumentation> resourcesDoc = new ArrayList<>();
      ToAPIResourceDocumentation resourceDoc = new ToAPIResourceDocumentation();
      resourceDoc.setLocale("en");
      resourceDoc.setApiResourceName(OpenApiConstants.SWAGGER_JSON);
      resourceDoc.setMimeType("HTML");
      final String encodedContent = getEncodedContent(data, api);
      resourceDoc.setContent(encodedContent);
      resourcesDoc.add(resourceDoc);
      return resourcesDoc;
    }
    return Collections.emptyList();
  }

  private static String getEncodedContent(String contentDecoded, API api) {
    JsonElement resDocJson = JsonParser.parseString(contentDecoded);
    JsonObject docJson = resDocJson.getAsJsonObject();
    OASVersion type = OASServiceFactory.getOASVersion(docJson);
    OASService oasServices = OASServiceFactory.getService(type);
    return oasServices.update(docJson, api);
  }

  private static List<ToAPIResources> getToAPIResources(
          List<ToAPIResourceDocumentation> resourcesDoc) {

    if (resourcesDoc != null && !resourcesDoc.isEmpty()) {
      List<ToAPIResources> apiResources = new ArrayList<>();
      ToAPIResources apiResource = new ToAPIResources();
      apiResource.setName(OpenApiConstants.SWAGGER_JSON);
      apiResource.setTitle(OpenApiConstants.SWAGGER_JSON);
      apiResource.setDescription(OpenApiConstants.SWAGGER_JSON);
      apiResource.setCanShowDelete(true);
      apiResource.setCanShowGet(true);
      apiResource.setCanShowPost(true);
      apiResource.setCanShowPut(true);
      apiResource.setCanShowOption(true);
      apiResource.setCanShowHead(true);
      apiResource.setCanShowPatch(true);
      apiResource.setResourcePath("/SWAGGER_JSON");
      apiResource.setResourceDocumentations(resourcesDoc);
      apiResources.add(apiResource);
      return apiResources;
    }
    return Collections.emptyList();
  }


  private static String getDescriptionFromOAS(List<ToAPIResourceDocumentation> resourcesDoc) {
    try {
      for (ToAPIResourceDocumentation doc : resourcesDoc) {
        final String contentDecoded = TransformerUtil.decodeJson(doc.getContent());
        JsonElement resDocJson = JsonParser.parseString(contentDecoded);
        JsonObject docJson = resDocJson.getAsJsonObject();
        OASVersion type = OASServiceFactory.getOASVersion(docJson);
        OASService oasServices = OASServiceFactory.getService(type);
        String description = oasServices.description(docJson);
        if (description != null && description.length() > 1024) {
          description = description.substring(0, 1023);
        }
        return description;
      }
    } catch (Exception e) {
      log.error("Failed to get the description from the Open API specification");
    }
    return null;
  }
}
