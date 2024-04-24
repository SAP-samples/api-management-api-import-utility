package com.apimgmt.gateway.model.input;


import com.apimgmt.gateway.model.EntityGenericClass;
import com.apimgmt.gateway.model.IPayloadModel;
import com.apimgmt.gateway.model.output.APIProxyData;
import com.google.gson.annotations.SerializedName;

public class UnmanagedAPIProxyIM extends EntityGenericClass implements IPayloadModel {
  private String apiVersionGroup;
  private String cfBindingId;
  private boolean isCopy;
  private boolean isPublished;
  private boolean isUnmanaged;
  private boolean isVersioned;
  private String name;
  @SerializedName("provider_name")
  private String providerName;
  private String releaseMetadata;
  private String releaseStatus;
  @SerializedName("service_code")
  private String serviceCode;
  private String state;
  @SerializedName("status_code")
  private String statusCode;
  private String version;
  private String title;
  private String description;
  private ProxyEndPointsIM proxyEndPoints;

  public String getApiVersionGroup() {
    return apiVersionGroup;
  }

  public void setApiVersionGroup(final String apiVersionGroup) {
    this.apiVersionGroup = apiVersionGroup;
  }

  public String getCfBindingId() {
    return cfBindingId;
  }

  public void setCfBindingId(final String cfBindingId) {
    this.cfBindingId = cfBindingId;
  }

  public boolean isCopy() {
    return isCopy;
  }

  public void setCopy(final boolean isCopy) {
    this.isCopy = isCopy;
  }

  public boolean isPublished() {
    return isPublished;
  }

  public void setPublished(final boolean isPublished) {
    this.isPublished = isPublished;
  }

  public boolean isUnmanaged() {
    return isUnmanaged;
  }

  public void setUnmanaged(final boolean isUnmanaged) {
    this.isUnmanaged = isUnmanaged;
  }

  public boolean isVersioned() {
    return isVersioned;
  }

  public void setVersioned(final boolean isVersioned) {
    this.isVersioned = isVersioned;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(final String providerName) {
    this.providerName = providerName;
  }

  public String getReleaseMetadata() {
    return releaseMetadata;
  }

  public void setReleaseMetadata(final String releaseMetadata) {
    this.releaseMetadata = releaseMetadata;
  }

  public String getReleaseStatus() {
    return releaseStatus;
  }

  public void setReleaseStatus(final String releaseStatus) {
    this.releaseStatus = releaseStatus;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(final String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(final String statusCode) {
    this.statusCode = statusCode;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
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

  public ProxyEndPointsIM getProxyEndPoints() {
    return proxyEndPoints;
  }

  public void setProxyEndPoints(final ProxyEndPointsIM proxyEndPoints) {
    this.proxyEndPoints = proxyEndPoints;
  }

  @Override
  public APIProxyData getPaylod() {
    return new APIProxyData(this);
  }

  @Override
  public String getId() {
    return this.name;
  }

  @Override
  public Long getLastModified() {
    return this.getChangedAt();
  }
}
