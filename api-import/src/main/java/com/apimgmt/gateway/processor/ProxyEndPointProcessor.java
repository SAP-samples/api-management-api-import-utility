package com.apimgmt.gateway.processor;

import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.client.BatchRequest;
import com.apimgmt.gateway.client.HttpMethod;
import com.apimgmt.gateway.comparator.DiffEntity;
import com.apimgmt.gateway.comparator.EntityComparator;
import com.apimgmt.gateway.model.output.ToAPIResources;
import com.apimgmt.gateway.model.output.ToProxyEndPoint;
import com.apimgmt.gateway.util.GsonProvider;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyEndPointProcessor {
  private APIResourceProcessor apiResourceProcessor;

  public ProxyEndPointProcessor() {
    this.apiResourceProcessor = new APIResourceProcessor();
  }

  public List<BatchRequest> update(DiffEntity diffEntity, List<ToProxyEndPoint> destEntityData,
                                   String parentId) {

    if (diffEntity == null) {
      return Collections.emptyList();
    }
    List<BatchRequest> batchRequests = new ArrayList<>();
    if (null != diffEntity.getEntityToCreate()) {
      ToProxyEndPoint entityToCreate = (ToProxyEndPoint) diffEntity.getEntityToCreate();
      log.error("Create of APIProxyEndpoint is not supported : {}", entityToCreate);
    }
    if (null != diffEntity.getEntityToDelete()) {
      log.error("Delete of APIProxyEndpoint is not supported : {}",
          diffEntity.getEntityToDelete().getModelId());
    }
    if (null != diffEntity.getEntityToUpdate()) {
      Gson gson = GsonProvider.getGson();
      ToProxyEndPoint srcEntity = (ToProxyEndPoint) diffEntity.getEntityToUpdate();
      ToProxyEndPoint dstEntity = destEntityData.get(0);
      String updateProxyEndPointUri = String.format(BatchConstants.API_PROXY_END_POINTS,
          dstEntity.getModelId());
      batchRequests.add(new BatchRequest(updateProxyEndPointUri,
          HttpMethod.PUT, gson.toJson(srcEntity)));
      log.info("Added proxy endPoint update request for Proxy :  {} ", parentId);
      // Update resources
      final List<BatchRequest> updateResChangeSets =
          getBatchRequestForResource(srcEntity, dstEntity);
      batchRequests.addAll(updateResChangeSets);

    }
    return batchRequests;
  }

  private List<BatchRequest> getBatchRequestForResource(ToProxyEndPoint srcEntity,
                                                        ToProxyEndPoint destEntity) {
    final String destEpId = destEntity.getModelId();
    List<ToAPIResources> apiResourceDst = destEntity.getApiResources();
    List<ToAPIResources> apiResourceSrc = srcEntity.getApiResources();
    final DiffEntity diffDocEntity = EntityComparator
        .compareEntity(apiResourceSrc, apiResourceDst);
    return apiResourceProcessor.update(diffDocEntity, apiResourceDst, destEpId);
  }

}
