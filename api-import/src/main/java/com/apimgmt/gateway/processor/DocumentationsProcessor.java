package com.apimgmt.gateway.processor;

import com.apimgmt.gateway.client.BatchConstants;
import com.apimgmt.gateway.client.BatchRequest;
import com.apimgmt.gateway.client.HttpMethod;
import com.apimgmt.gateway.comparator.DiffEntity;
import com.apimgmt.gateway.model.output.ToAPIResourceDocumentation;
import com.apimgmt.gateway.util.GsonProvider;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentationsProcessor {
  public List<BatchRequest> update(DiffEntity diffEntity,
                                   List<ToAPIResourceDocumentation> documentationDst,
                                   String parentId) {
    if (diffEntity == null) {
      return Collections.emptyList();
    }
    List<BatchRequest> batchRequests = new ArrayList<>();
    Gson gson = GsonProvider.getGson();
    if (null != diffEntity.getEntityToUpdate()) {
      ToAPIResourceDocumentation dstEntity = documentationDst.get(0);
      String updateDocumentUri =
          String.format(BatchConstants.PUT_DOCUMENTATION, dstEntity.getModelId(), "en");
      batchRequests.add(new BatchRequest(updateDocumentUri, HttpMethod.PUT,
          gson.toJson(diffEntity.getEntityToUpdate())));
      log.info("Added documentation update for resourceId :  {} ", parentId);
    }
    return batchRequests;
  }
}
