package com.apimgmt.gateway.model.input;

import com.apimgmt.gateway.model.AdditionalProperty;
import com.apimgmt.gateway.model.EntityGenericClass;
import com.apimgmt.gateway.model.IPayloadModel;
import com.apimgmt.gateway.model.ResultModel;
import com.apimgmt.gateway.model.output.APIProductOM;
import com.google.gson.annotations.SerializedName;


public class APIProductIM extends EntityGenericClass implements IPayloadModel {

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
  private ResultModel<APIProductGenericIM> apiProxies;
  private ResultModel<APIProductGenericIM> ratePlans;
  private ResultModel<APIProductGenericIM> apiResources;
  private ResultModel<AdditionalProperty> additionalProperties;

  public void setName(final String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public Boolean getIsPublished() {
    return isPublished;
  }

  public void setIsPublished(final Boolean isPublished) {
    this.isPublished = isPublished;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final String statusCode) {
    this.statusCode = statusCode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public Boolean getIsRestricted() {
    return isRestricted;
  }

  public void setIsRestricted(final Boolean isRestricted) {
    this.isRestricted = isRestricted;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

  public Integer getQuotaCount() {
    return quotaCount;
  }

  public void setQuotaCount(final Integer quotaCount) {
    this.quotaCount = quotaCount;
  }

  public Integer getQuotaInterval() {
    return quotaInterval;
  }

  public void setQuotaInterval(final Integer quotaInterval) {
    this.quotaInterval = quotaInterval;
  }

  public String getQuotaTimeUnit() {
    return quotaTimeUnit;
  }

  public void setQuotaTimeUnit(final String quotaTimeUnit) {
    this.quotaTimeUnit = quotaTimeUnit;
  }

  public ResultModel<APIProductGenericIM> getApiProxies() {
    return apiProxies;
  }

  public void setApiProxies(final ResultModel<APIProductGenericIM> apiProxies) {
    this.apiProxies = apiProxies;
  }

  public ResultModel<APIProductGenericIM> getRatePlans() {
    return ratePlans;
  }

  public void setRatePlans(final ResultModel<APIProductGenericIM> ratePlans) {
    this.ratePlans = ratePlans;
  }

  public ResultModel<APIProductGenericIM> getApiResources() {
    return apiResources;
  }

  public void setApiResources(final ResultModel<APIProductGenericIM> apiResources) {
    this.apiResources = apiResources;
  }

  public ResultModel<AdditionalProperty> getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(final ResultModel<AdditionalProperty> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  @Override
  public String getName() {
    return title;
  }

  @Override
  public String getId() {
    return name;
  }

  @Override
  public Long getLastModified() {
    return getChangedAt();
  }

  @Override
  public APIProductOM getPaylod() {
    return new APIProductOM(this);
  }

}
