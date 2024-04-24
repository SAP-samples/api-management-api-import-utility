package com.apimgmt.gateway.openapi;

import com.google.gson.JsonObject;

public class OASServiceFactory {

  public static OASService getService(final OASVersion type) {
    switch (type) {
      case OAS2:
        return new OAS2Operation();
      default:
        return new OAS3Operation();
    }
  }

  public static OASVersion getOASVersion(final JsonObject docJson) {
    if (docJson != null && docJson.get(OpenApiConstants.OPEN_API_V3) != null
        && docJson.get(OpenApiConstants.OPEN_API_V3).getAsString().startsWith("3.0")) {
      return OASVersion.OAS3;
    } else {
      return OASVersion.OAS2;
    }
  }
}
