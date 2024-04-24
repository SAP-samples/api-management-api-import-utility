package com.apimgmt.gateway.model.output;


import com.apimgmt.gateway.model.input.APIResourceIM;
import com.apimgmt.gateway.model.input.APIResourcesIM;
import com.apimgmt.gateway.model.input.DocumentationIM;
import com.apimgmt.gateway.model.input.DocumentationsIM;
import com.apimgmt.gateway.model.input.ProxyEndPointIM;
import com.apimgmt.gateway.model.input.UnmanagedAPIProxyIM;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class APIProxyData {

  private String name;
  private String version;
  private String title;
  private String releaseStatus = "Active";
  private String releaseMetadata = "{\"reason\":\"\"}";

  private String description;
  private boolean isUnmanaged = true;
  private boolean isPublished = false;
  @SerializedName("service_code")
  private String serviceCode = "REST";
  @SerializedName("provider_name")
  private String providerName = "NONE";
  @SerializedName("status_code")
  private String statusCode = "REGISTERED";
  private String state = "EXTERNAL";
  @SerializedName("proxyEndPoints")
  private List<ToProxyEndPoint> proxyEndPoints;
  @SerializedName("__metadata")
  private Metadata metadata;

  public APIProxyData(final UnmanagedAPIProxyIM im) {
    this.name = im.getName();
    this.version = im.getVersion();
    this.title = im.getTitle();
    this.releaseStatus = im.getReleaseStatus();
    this.releaseMetadata = im.getReleaseMetadata();
    this.description = im.getDescription();
    this.isUnmanaged = im.isUnmanaged();
    this.serviceCode = im.getServiceCode();
    this.providerName = im.getProviderName();
    this.statusCode = im.getStatusCode();
    this.state = im.getState();
    this.proxyEndPoints = new ArrayList<>();
    if (im.getProxyEndPoints().getResults() != null) {
      for (ProxyEndPointIM inputPEP : im.getProxyEndPoints().getResults()) {
        // getProxyEndPoint
        ToProxyEndPoint outputPEP = getToProxyEndPoint(inputPEP);
        // getApiResource
        final List<ToAPIResources> apiResourceOMs = getToAPIResources(inputPEP.getApiResources());
        outputPEP.setApiResources(apiResourceOMs);
        this.proxyEndPoints.add(outputPEP);
      }
    }
  }

  private List<ToAPIResources> getToAPIResources(APIResourcesIM apiResources) {
    List<ToAPIResources> apiResourceOMs = new ArrayList<>();
    if (apiResources.getResults() != null) {
      for (APIResourceIM inRes : apiResources.getResults()) {
        final ToAPIResources apiResourceOM = getAPIResource(inRes);
        //add Documentation
        final List<ToAPIResourceDocumentation> docs =
            getToAPIResourceDocumentations(inRes.getDocumentations());
        apiResourceOM.setResourceDocumentations(docs);
        apiResourceOMs.add(apiResourceOM);
      }
    }
    return apiResourceOMs;
  }

  private List<ToAPIResourceDocumentation> getToAPIResourceDocumentations(
      DocumentationsIM documentations) {
    List<ToAPIResourceDocumentation> documentationOMs = new ArrayList<>();
    if (documentations.getResults() != null) {
      for (DocumentationIM inDoc : documentations.getResults()) {
        ToAPIResourceDocumentation documentationOM = getToAPIResourceDocumentation(inDoc);
        documentationOMs.add(documentationOM);
      }
    }
    return documentationOMs;
  }

  private ToProxyEndPoint getToProxyEndPoint(ProxyEndPointIM inputPEP) {
    ToProxyEndPoint outputPEP = new ToProxyEndPoint();
    outputPEP.setBasePath(inputPEP.getBasePath());
    outputPEP.setDefault(inputPEP.isDefault());
    outputPEP.setName(inputPEP.getName());
    outputPEP.setPublishUrl(inputPEP.getPublishUrl());
    outputPEP.setId(inputPEP.getId());
    return outputPEP;
  }

  private ToAPIResourceDocumentation getToAPIResourceDocumentation(
      DocumentationIM inputDocumenatation) {
    ToAPIResourceDocumentation documentationOM = new ToAPIResourceDocumentation();
    documentationOM.setApiResourceName(inputDocumenatation.getApiResourceName());
    documentationOM.setContent(inputDocumenatation.getContent());
    documentationOM.setLocale(inputDocumenatation.getLocale());
    documentationOM.setMimeType(inputDocumenatation.getMimeType());
    documentationOM.setId(inputDocumenatation.getId());
    return documentationOM;
  }

  private ToAPIResources getAPIResource(APIResourceIM inputResource) {
    ToAPIResources apiResourceOM = new ToAPIResources();
    apiResourceOM.setCanShowGet(inputResource.isCanShowGet());
    apiResourceOM.setCanShowPut(inputResource.isCanShowPut());
    apiResourceOM.setCanShowPost(inputResource.isCanShowPost());
    apiResourceOM.setCanShowDelete(inputResource.isCanShowDelete());
    apiResourceOM.setCanShowHead(inputResource.isCanShowHead());
    apiResourceOM.setCanShowOption(inputResource.isCanShowOption());
    apiResourceOM.setCanShowPatch(inputResource.isCanShowPatch());

    apiResourceOM.setGetChecked(inputResource.isGetChecked());
    apiResourceOM.setPutChecked(inputResource.isPutChecked());
    apiResourceOM.setPostChecked(inputResource.isPostChecked());
    apiResourceOM.setDeleteChecked(inputResource.isDeleteChecked());
    apiResourceOM.setHeadChecked(inputResource.isHeadChecked());
    apiResourceOM.setOptionChecked(inputResource.isOptionChecked());
    apiResourceOM.setPatchChecked(inputResource.isPatchChecked());

    apiResourceOM.setDescription(inputResource.getDescription());
    apiResourceOM.setName(inputResource.getName());
    apiResourceOM.setResourcePath(inputResource.getResourcePath());
    apiResourceOM.setTitle(inputResource.getTitle());
    apiResourceOM.setId(inputResource.getId());
    return apiResourceOM;
  }


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


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getServiceCode() {
    return serviceCode;
  }


  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }


  public boolean isUnmanaged() {
    return isUnmanaged;
  }


  public void setUnmanaged(boolean isUnmanaged) {
    this.isUnmanaged = isUnmanaged;
  }

  public boolean isPublished() {
    return isPublished;
  }

  public void setPublished(boolean published) {
    isPublished = published;
  }

  public String getReleaseStatus() {
    return releaseStatus;
  }


  public void setReleaseStatus(String releaseStatus) {
    this.releaseStatus = releaseStatus;
  }


  public String getReleaseMetadata() {
    return releaseMetadata;
  }


  public void setReleaseMetadata(String releaseMetadata) {
    this.releaseMetadata = releaseMetadata;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public List<ToProxyEndPoint> getProxyEndPoints() {
    return proxyEndPoints;
  }


  public void setProxyEndPoints(List<ToProxyEndPoint> proxyEndPoints) {
    this.proxyEndPoints = proxyEndPoints;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    APIProxyData that = (APIProxyData) o;
    return Objects.equals(name, that.name) && Objects.equals(version, that.version)
        && Objects.equals(title, that.title) && Objects.equals(releaseStatus, that.releaseStatus)
        && Objects.equals(proxyEndPoints, that.proxyEndPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, version, title, releaseStatus, proxyEndPoints);
  }
}
