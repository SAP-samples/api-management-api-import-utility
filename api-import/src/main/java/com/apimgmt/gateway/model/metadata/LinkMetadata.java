package com.apimgmt.gateway.model.metadata;

import com.apimgmt.gateway.model.output.URIMetadata;
import com.google.gson.annotations.SerializedName;


public class LinkMetadata {

  @SerializedName("__metadata")
  private URIMetadata metadata;

  public URIMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(URIMetadata metadata) {
    this.metadata = metadata;
  }
}
