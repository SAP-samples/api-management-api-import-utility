package com.apimgmt.gateway.model.input;

import com.google.gson.annotations.SerializedName;

public class ProxyEndPointIM {
  private String name = "default";
  private boolean isDefault = true;
  private String publishUrl;
  @SerializedName("base_path")
  private String basePath;
  private String id;
  private APIResourcesIM apiResources;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(final boolean isDefault) {
    this.isDefault = isDefault;
  }

  public String getPublishUrl() {
    return publishUrl;
  }

  public void setPublishUrl(final String publishUrl) {
    this.publishUrl = publishUrl;
  }

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public APIResourcesIM getApiResources() {
    return apiResources;
  }

  public void setApiResources(final APIResourcesIM results) {
    this.apiResources = results;
  }
}
