package com.apimgmt.aam.transformer;

import com.apimgmt.aam.exception.AAMManagementBusinessException;
import com.common.model.API;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


/*
 *
 * List of Api discovery is done via Api Manager which is Design Time
 * Api definition is from the exchange
 *
 *
 */
@Slf4j
public class AAMTransformer {


  public static List<API> transformToAPI(JsonArray apiList)
    throws AAMManagementBusinessException {
    List<API> response = new ArrayList<>();
    for (JsonElement apiInfo : apiList) {
      API api = apiResponse(apiInfo);
      response.add(api);
    }
    return response;
  }

  public static API apiResponse(JsonElement apiInfo) throws AAMManagementBusinessException {
    try {
      String name = null, protocol = null, urlEndPoint = null, description = null, status = null,
        version = null, type = null, serviceUrlTemp = null, path = null;
      boolean isCurrent = false;
      JsonObject json = apiInfo.getAsJsonObject();
      String apiIdName = json.get("name").getAsString();
      type = json.get("type").getAsString();
      if (json.has("properties") && !json.get("properties").isJsonNull()) {
        final JsonObject properties = json.get("properties").getAsJsonObject();
        if (properties.has("displayName") && !properties.get("displayName").isJsonNull()) {
          name = properties.get("displayName").getAsString();
        }
        if (properties.has("protocols") && !properties.get("protocols").isJsonNull()) {
          protocol = properties.getAsJsonArray("protocols").get(0).getAsString();
        }
        if (properties.has("serviceUrl") && !properties.get("serviceUrl").isJsonNull()) {
          serviceUrlTemp = properties.get("serviceUrl").getAsString();
        }
        if (properties.has("path") && !properties.get("path").isJsonNull()) {
          path = properties.get("path").getAsString();
        }
        if (serviceUrlTemp == null) {
          urlEndPoint = "Https://" + path;
        } else {
          urlEndPoint = properties.get("serviceUrl").getAsString() + "/" + path;
        }

        if (properties.has("isCurrent") && !properties.get("isCurrent").isJsonNull()) {
          isCurrent = properties.get("isCurrent").getAsBoolean();
        }
        if (properties.has("apiVersion") && !properties.get("apiVersion").isJsonNull()) {
          version = properties.get("apiVersion").getAsString();
        }
        if (properties.has("description") && !properties.get("description").isJsonNull()) {
          description = properties.get("description").getAsString();
        }
      }
      status = isCurrent ? "active" : "inactive";

      API api = new API();
      api.setApiId(apiIdName);
      api.setApiName(apiIdName);
      api.setStatus(status);
      api.setDescription(description);
      api.setVersion(version);
      api.setApiType(type);
      api.setProtocol("REST");
      api.setEntryPoint(urlEndPoint);
      return api;
    } catch (Exception e) {
      throw new AAMManagementBusinessException("Error in Parsing API response", e);
    }
  }
}
