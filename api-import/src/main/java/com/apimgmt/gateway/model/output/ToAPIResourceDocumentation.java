package com.apimgmt.gateway.model.output;

import com.apimgmt.gateway.comparator.ModelEntity;
import com.apimgmt.gateway.util.Exclude;
import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.util.Objects;

@ToString
public class ToAPIResourceDocumentation implements ModelEntity {
  private String locale;
  private String apiResourceName;
  @SerializedName("mime_type")
  private String mimeType;
  private String content;
  @Exclude
  private String id;

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getApiResourceName() {
    return apiResourceName;
  }

  public void setApiResourceName(String apiResourceName) {
    this.apiResourceName = apiResourceName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToAPIResourceDocumentation that = (ToAPIResourceDocumentation) o;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public String getModelId() {
    return id;
  }

}
