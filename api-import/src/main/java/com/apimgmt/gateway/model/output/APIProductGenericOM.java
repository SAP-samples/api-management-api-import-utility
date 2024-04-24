package com.apimgmt.gateway.model.output;

import com.apimgmt.gateway.util.Exclude;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class APIProductGenericOM {
  @SerializedName("__metadata")
  private URIMetadata metadata;

  @Exclude
  private String name;
}
