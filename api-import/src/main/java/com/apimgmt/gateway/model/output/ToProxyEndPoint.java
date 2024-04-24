package com.apimgmt.gateway.model.output;

import com.apimgmt.gateway.comparator.ModelEntity;
import com.apimgmt.gateway.model.metadata.LinkMetadata;
import com.apimgmt.gateway.util.Exclude;
import com.apimgmt.gateway.util.Link;
import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class ToProxyEndPoint implements ModelEntity {
  private String publishUrl;
  private String name;
  private boolean isDefault;

  @SerializedName("base_path")
  private String basePath;
  @Exclude
  private String id;
  @SerializedName("apiProxy")
  @Link
  private List<LinkMetadata> apiProxy;
  @SerializedName("apiResources")
  private List<ToAPIResources> apiResources;

  public String getPublishUrl() {
    return publishUrl;
  }

  public void setPublishUrl(String publishUrl) {
    this.publishUrl = publishUrl;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean aDefault) {
    isDefault = aDefault;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public List<ToAPIResources> getApiResources() {
    return apiResources;
  }

  public void setApiResources(List<ToAPIResources> apiResources) {
    this.apiResources = apiResources;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<LinkMetadata> getApiProxy() {
    return apiProxy;
  }

  public void setApiProxy(List<LinkMetadata> apiProxy) {
    this.apiProxy = apiProxy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToProxyEndPoint that = (ToProxyEndPoint) o;
    return Objects.equals(publishUrl, that.publishUrl)
        && Objects.equals(basePath, that.basePath)
        && Objects.equals(apiResources, that.apiResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publishUrl, basePath, apiResources);
  }

  @Override
  public String getModelId() {
    return id;
  }

}
