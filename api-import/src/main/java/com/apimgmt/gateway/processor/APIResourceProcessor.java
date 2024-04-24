package com.apimgmt.gateway.processor;

import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.client.BatchRequest;
import com.apimgmt.gateway.client.HttpMethod;
import com.apimgmt.gateway.comparator.DiffEntity;
import com.apimgmt.gateway.comparator.EntityComparator;
import com.apimgmt.gateway.model.metadata.LinkMetadata;
import com.apimgmt.gateway.model.output.ToAPIResourceDocumentation;
import com.apimgmt.gateway.model.output.ToAPIResources;
import com.apimgmt.gateway.model.output.URIMetadata;
import com.apimgmt.gateway.util.GsonProvider;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIResourceProcessor {

  private DocumentationsProcessor documentationsProcessor;

  public APIResourceProcessor() {
    this.documentationsProcessor = new DocumentationsProcessor();
  }

  public List<BatchRequest> update(DiffEntity diffEntity, List<ToAPIResources> destEntityData,
                                   String parentId) {

    if (diffEntity == null) {
      return Collections.emptyList();
    }
    List<BatchRequest> batchRequests = new ArrayList<>();
    Gson gson = GsonProvider.getGson();
    Gson gsonWithLink = GsonProvider.getGsonWithLink();
    if (null != diffEntity.getEntityToCreate()) {
      ToAPIResources entityToCreate = (ToAPIResources) diffEntity.getEntityToCreate();
      List<LinkMetadata> proxies = getLinkMetadata(parentId);
      entityToCreate.setApiProxyEndPoint(proxies);
      batchRequests.add(new BatchRequest(BatchConstants.ADD_RESOURCES,
          HttpMethod.POST, gsonWithLink.toJson(entityToCreate)));
      log.info("Added Resource Create for ProxyEndPointId :  {} ", parentId);
    }

    if (null != diffEntity.getEntityToDelete()) {
      String deleteResourceUri = String.format(BatchConstants.API_RESOURCES,
          diffEntity.getEntityToDelete().getModelId());
      batchRequests.add(new BatchRequest(deleteResourceUri,
          HttpMethod.DELETE, gson.toJson(diffEntity.getEntityToDelete())));
      log.info("Added resource delete for proxy endPoint id :  {} ", parentId);
    }

    if (null != diffEntity.getEntityToUpdate()) {
      ToAPIResources srcEntity = (ToAPIResources) diffEntity.getEntityToUpdate();
      ToAPIResources destEntity = destEntityData.get(0);
      List<ToAPIResourceDocumentation> documentationSrc = srcEntity.getResourceDocumentations();
      List<ToAPIResourceDocumentation> documentationDst = destEntity.getResourceDocumentations();
      final String destResourceId = destEntity.getId();
      final DiffEntity diffDocEntity =
          EntityComparator.compareEntity(documentationSrc, documentationDst);
      final List<BatchRequest> updateResourceChangeSets = documentationsProcessor
          .update(diffDocEntity, documentationDst, destResourceId);
      batchRequests.addAll(updateResourceChangeSets);
    }
    return batchRequests;
  }

  private static List<LinkMetadata> getLinkMetadata(String parentId) {
    LinkMetadata proxy = new LinkMetadata();
    URIMetadata data = new URIMetadata();
    String uriName = String.format(BatchConstants.API_PROXY_END_POINTS, parentId);
    data.setUri(uriName);
    proxy.setMetadata(data);
    return Collections.singletonList(proxy);
  }

}
