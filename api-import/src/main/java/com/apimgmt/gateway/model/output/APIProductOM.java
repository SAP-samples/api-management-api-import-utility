package com.apimgmt.gateway.model.output;

import com.apimgmt.gateway.model.input.APIProductGenericIM;
import com.apimgmt.gateway.model.input.APIProductIM;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class APIProductOM {

  private String name;
  private String version;
  private Boolean isPublished;
  @SerializedName("status_code")
  private String statusCode;
  private String title;
  private String description;
  private Boolean isRestricted;
  private String scope;
  private Integer quotaCount;
  private Integer quotaInterval;
  private String quotaTimeUnit;
  private List<String> apiProxies;

  public APIProductOM(final APIProductIM payload) {
    super();
    name = payload.getId();
    version = payload.getVersion();
    isPublished = payload.getIsPublished();
    statusCode = payload.getStatusCode();
    title = payload.getTitle();
    description = payload.getDescription();
    isRestricted = payload.getIsRestricted();
    scope = payload.getScope();
    quotaCount = payload.getQuotaCount();
    quotaInterval = payload.getQuotaInterval();
    quotaTimeUnit = payload.getQuotaTimeUnit();
    apiProxies = new ArrayList<>();
    for (APIProductGenericIM proxy : payload.getApiProxies().getResults()) {
      apiProxies.add(proxy.getName());
    }
  }
}
