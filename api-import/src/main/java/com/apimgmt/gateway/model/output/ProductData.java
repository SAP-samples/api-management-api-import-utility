package com.apimgmt.gateway.model.output;

import com.apimgmt.gateway.model.metadata.LinkMetadata;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class ProductData {

  private String name;
  private String version;
  private boolean isPublished;
  @SerializedName("status_code")
  private String statusCode;
  private String title;
  private String shortText;
  private String description;
  private boolean isRestricted;
  private String scope;
  private String quotaCount;
  private String quotaInterval;
  private String quotaTimeUnit;
  @SerializedName("apiProxies")
  private List<LinkMetadata> toAPIProxies;
  @SerializedName("__metadata")
  private Metadata metadata;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public boolean isPublished() {
    return isPublished;
  }

  public void setPublished(boolean published) {
    isPublished = published;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getShortText() {
    return shortText;
  }

  public void setShortText(String shortText) {
    this.shortText = shortText;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isRestricted() {
    return isRestricted;
  }

  public void setRestricted(boolean restricted) {
    isRestricted = restricted;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getQuotaCount() {
    return quotaCount;
  }

  public void setQuotaCount(String quotaCount) {
    this.quotaCount = quotaCount;
  }

  public String getQuotaInterval() {
    return quotaInterval;
  }

  public void setQuotaInterval(String quotaInterval) {
    this.quotaInterval = quotaInterval;
  }

  public String getQuotaTimeUnit() {
    return quotaTimeUnit;
  }

  public void setQuotaTimeUnit(String quotaTimeUnit) {
    this.quotaTimeUnit = quotaTimeUnit;
  }

  public List<LinkMetadata> getToAPIProxies() {
    return toAPIProxies;
  }

  public void setToAPIProxies(List<LinkMetadata> toAPIProxies) {
    this.toAPIProxies = toAPIProxies;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }
}
