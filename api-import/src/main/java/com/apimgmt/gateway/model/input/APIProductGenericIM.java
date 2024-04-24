package com.apimgmt.gateway.model.input;

import com.apimgmt.gateway.model.metadata.MetaData;
import com.google.gson.annotations.SerializedName;

public class APIProductGenericIM {
  private String name;
  private String id;
  @SerializedName("__metadata")
  private MetaData metadata;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public MetaData getMetadata() {
    return metadata;
  }

  public void setMetadata(final MetaData metadata) {
    this.metadata = metadata;
  }

}
