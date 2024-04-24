package com.apimgmt.gateway.model.input;

import com.google.gson.annotations.SerializedName;

public class APIResourceIM {
  @SerializedName("resource_path")
  private String resourcePath;
  private String title;
  private String name;
  private String description;
  private boolean canShowGet = true;
  private boolean canShowPost = true;
  private boolean canShowPut = true;
  private boolean canShowDelete = true;
  private boolean canShowHead = true;
  private boolean canShowOption = true;
  private boolean canShowPatch = true;
  private boolean isGetChecked = false;
  private boolean isPostChecked = false;
  private boolean isPutChecked = false;
  private boolean isDeleteChecked = false;
  private boolean isHeadChecked = false;
  private boolean isOptionChecked = false;
  private boolean isPatchChecked = false;
  private String id;
  private DocumentationsIM documentations;

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(final String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public boolean isCanShowGet() {
    return canShowGet;
  }

  public void setCanShowGet(final boolean canShowGet) {
    this.canShowGet = canShowGet;
  }

  public boolean isCanShowPost() {
    return canShowPost;
  }

  public void setCanShowPost(final boolean canShowPost) {
    this.canShowPost = canShowPost;
  }

  public boolean isCanShowPut() {
    return canShowPut;
  }

  public void setCanShowPut(final boolean canShowPut) {
    this.canShowPut = canShowPut;
  }

  public boolean isCanShowDelete() {
    return canShowDelete;
  }

  public void setCanShowDelete(final boolean canShowDelete) {
    this.canShowDelete = canShowDelete;
  }

  public boolean isCanShowHead() {
    return canShowHead;
  }

  public void setCanShowHead(final boolean canShowHead) {
    this.canShowHead = canShowHead;
  }

  public boolean isCanShowOption() {
    return canShowOption;
  }

  public void setCanShowOption(final boolean canShowOption) {
    this.canShowOption = canShowOption;
  }

  public boolean isCanShowPatch() {
    return canShowPatch;
  }

  public void setCanShowPatch(final boolean canShowPatch) {
    this.canShowPatch = canShowPatch;
  }

  public boolean isGetChecked() {
    return isGetChecked;
  }

  public void setGetChecked(final boolean isGetChecked) {
    this.isGetChecked = isGetChecked;
  }

  public boolean isPostChecked() {
    return isPostChecked;
  }

  public void setPostChecked(final boolean isPostChecked) {
    this.isPostChecked = isPostChecked;
  }

  public boolean isPutChecked() {
    return isPutChecked;
  }

  public void setPutChecked(final boolean isPutChecked) {
    this.isPutChecked = isPutChecked;
  }

  public boolean isDeleteChecked() {
    return isDeleteChecked;
  }

  public void setDeleteChecked(final boolean isDeleteChecked) {
    this.isDeleteChecked = isDeleteChecked;
  }

  public boolean isHeadChecked() {
    return isHeadChecked;
  }

  public void setHeadChecked(final boolean isHeadChecked) {
    this.isHeadChecked = isHeadChecked;
  }

  public boolean isOptionChecked() {
    return isOptionChecked;
  }

  public void setOptionChecked(final boolean isOptionChecked) {
    this.isOptionChecked = isOptionChecked;
  }

  public boolean isPatchChecked() {
    return isPatchChecked;
  }

  public void setPatchChecked(final boolean isPatchChecked) {
    this.isPatchChecked = isPatchChecked;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DocumentationsIM getDocumentations() {
    return documentations;
  }

  public void setDocumentations(final DocumentationsIM documentations) {
    this.documentations = documentations;
  }

}
