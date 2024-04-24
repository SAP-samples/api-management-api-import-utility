package com.common.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class API {
  private String apiId;
  private String apiName;
  private String description;
  private String entryPoint;
  private String apiType;
  private String protocol;
  private String version;
  private String status;
  private APIDefinition apiDefinition;

  @Override
  public String toString() {
    return "API{"
        + "apiId='" + apiId + '\''
        + ", apiName='" + apiName + '\''
        + ", description='" + description + '\''
        + ", entryPoint='" + entryPoint + '\''
        + ", apiType='" + apiType + '\''
        + ", protocol='" + protocol + '\''
        + ", version='" + version + '\''
        + ", status='" + status + '\''
        + ", apiDefinition='" + apiDefinition + '\''
        + '}';
  }
}