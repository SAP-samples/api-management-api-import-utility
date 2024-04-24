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
public class ToAPIResources implements ModelEntity {
  private String name;
  private String title;
  private String description;
  private boolean canShowDelete;
  private boolean canShowGet;
  private boolean canShowPost;
  private boolean canShowPut;
  private boolean canShowOption;
  private boolean canShowHead;
  private boolean canShowPatch;
  private boolean isGetChecked;
  private boolean isPostChecked;
  private boolean isPutChecked;
  private boolean isDeleteChecked;
  private boolean isOptionChecked;
  private boolean isHeadChecked;
  private boolean isPatchChecked;
  @SerializedName("resource_path")
  private String resourcePath;
  @Exclude
  private String id;
  @SerializedName("apiProxyEndPoint")
  @Link
  private List<LinkMetadata> apiProxyEndPoint;
  @SerializedName("documentations")
  private List<ToAPIResourceDocumentation> resourceDocumentations;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCanShowDelete() {
    return canShowDelete;
  }

  public void setCanShowDelete(boolean canShowDelete) {
    this.canShowDelete = canShowDelete;
  }

  public boolean isCanShowGet() {
    return canShowGet;
  }

  public void setCanShowGet(boolean canShowGet) {
    this.canShowGet = canShowGet;
  }

  public boolean isCanShowPost() {
    return canShowPost;
  }

  public void setCanShowPost(boolean canShowPost) {
    this.canShowPost = canShowPost;
  }

  public boolean isCanShowPut() {
    return canShowPut;
  }

  public void setCanShowPut(boolean canShowPut) {
    this.canShowPut = canShowPut;
  }

  public boolean isCanShowOption() {
    return canShowOption;
  }

  public void setCanShowOption(boolean canShowOption) {
    this.canShowOption = canShowOption;
  }

  public boolean isCanShowHead() {
    return canShowHead;
  }

  public void setCanShowHead(boolean canShowHead) {
    this.canShowHead = canShowHead;
  }

  public boolean isCanShowPatch() {
    return canShowPatch;
  }

  public void setCanShowPatch(boolean canShowPatch) {
    this.canShowPatch = canShowPatch;
  }

  public boolean isGetChecked() {
    return isGetChecked;
  }

  public void setGetChecked(boolean isGetChecked) {
    this.isGetChecked = isGetChecked;
  }

  public boolean isPostChecked() {
    return isPostChecked;
  }

  public void setPostChecked(boolean isPostChecked) {
    this.isPostChecked = isPostChecked;
  }

  public boolean isDeleteChecked() {
    return isDeleteChecked;
  }

  public void setDeleteChecked(boolean isDeleteChecked) {
    this.isDeleteChecked = isDeleteChecked;
  }

  public boolean isPutChecked() {
    return isPutChecked;
  }

  public void setPutChecked(boolean isPutChecked) {
    this.isPutChecked = isPutChecked;
  }

  public boolean isOptionChecked() {
    return isOptionChecked;
  }

  public void setOptionChecked(boolean isOptionChecked) {
    this.isOptionChecked = isOptionChecked;
  }

  public boolean isHeadChecked() {
    return isHeadChecked;
  }

  public void setHeadChecked(boolean isHeadChecked) {
    this.isHeadChecked = isHeadChecked;
  }

  public boolean isPatchChecked() {
    return isPatchChecked;
  }

  public void setPatchChecked(boolean isPatchChecked) {
    this.isPatchChecked = isPatchChecked;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<LinkMetadata> getApiProxyEndPoint() {
    return apiProxyEndPoint;
  }

  public void setApiProxyEndPoint(List<LinkMetadata> apiProxyEndPoint) {
    this.apiProxyEndPoint = apiProxyEndPoint;
  }

  public List<ToAPIResourceDocumentation> getResourceDocumentations() {
    return resourceDocumentations;
  }

  public void setResourceDocumentations(List<ToAPIResourceDocumentation> resourceDocumentations) {
    this.resourceDocumentations = resourceDocumentations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToAPIResources that = (ToAPIResources) o;
    return Objects.equals(resourceDocumentations, that.resourceDocumentations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceDocumentations);
  }

  @Override
  public String getModelId() {
    return id;
  }

}
