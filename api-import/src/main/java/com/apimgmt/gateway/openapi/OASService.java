package com.apimgmt.gateway.openapi;

import com.common.model.API;
import com.google.gson.JsonObject;

public interface OASService {

  /**
   * This method provides interface to create/update swagger JSON resource in API Proxy.<br>
   * This should fetch the API proxy content & add or update the swagger JSON resource by checking the
   * OAS version accordingly.
   *
   * @param docJson  - JSON object conataing data of existing SWagger JSON resource
   * @param document - Documentation form of exisitng swagger JSON in base64 format
   * @return APIResource - List of api resource with an extra resource added in SWAGGER_JSON containing OAS
   * @since 1.120.0
   */


  String update(final JsonObject docJson, final API proxy);

  String description(final JsonObject docJson);

}
