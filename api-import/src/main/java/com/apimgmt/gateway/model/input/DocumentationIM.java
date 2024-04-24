package com.apimgmt.gateway.model.input;

import com.google.gson.annotations.SerializedName;

public class DocumentationIM {
  private String content;
  @SerializedName("mime_type")
  private String mimeType = "HTML";
  private String apiResourceName = "SWAGGER_JSON";
  private String locale = "en";
  private String id;

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(final String mimeType) {
    this.mimeType = mimeType;
  }

  public String getApiResourceName() {
    return apiResourceName;
  }

  public void setApiResourceName(final String apiResourceName) {
    this.apiResourceName = apiResourceName;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(final String locale) {
    this.locale = locale;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }
}
