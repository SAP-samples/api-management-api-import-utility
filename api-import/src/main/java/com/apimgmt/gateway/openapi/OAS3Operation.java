package com.apimgmt.gateway.openapi;

import com.apimgmt.gateway.util.TransformerUtil;
import com.common.model.API;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAS3Operation implements OASService {


  @Override
  public String update(final JsonObject docJson, final API proxy) {
    boolean isJsonChanged = false;
    String encodedString = null;
    try {
      encodedString = TransformerUtil.encodeJson(docJson.toString());
      final JsonObject infoObj = docJson.get(OpenApiConstants.INFO).getAsJsonObject();
      final JsonElement versionInJson = infoObj.get(OpenApiConstants.VERSION);
      final JsonElement titleInJson = infoObj.get(OpenApiConstants.TITLE);
      final JsonElement apiDescInJson = infoObj.get(OpenApiConstants.DESCRIPTION);


      final String title = proxy.getApiName();
      if (title != null && ((titleInJson == null || titleInJson.getAsString().length() < 1)
          || !(title.equals(titleInJson.getAsString())))) {
        infoObj.addProperty(OpenApiConstants.TITLE, title);
        isJsonChanged = true;
      }
      final String proxyDesc = proxy.getDescription();
      if (proxyDesc != null && !proxyDesc.isEmpty()) {
        if (apiDescInJson == null || apiDescInJson.getAsString().length() < 1
            || !(proxyDesc.equals(apiDescInJson.getAsString()))) {
          infoObj.addProperty(OpenApiConstants.DESCRIPTION, proxyDesc);
          isJsonChanged = true;
        }
      }
      String proxyVersion = proxy.getVersion();
      if (proxyVersion != null
          && ((versionInJson == null || versionInJson.getAsString().length() < 1)
          || !(proxyVersion.equals(versionInJson.getAsString())))) {

        infoObj.addProperty(OpenApiConstants.VERSION, proxyVersion);
        isJsonChanged = true;
      }

      if (isJsonChanged) {
        return TransformerUtil.encodeJson(docJson.toString());
      }

    } catch (final JsonIOException | MalformedURLException e) {
      log.error("Encoding Open API Specification failed with error :{} ", e.getMessage());
    }
    return encodedString;
  }

  @Override
  public String description(JsonObject docJson) {
    final JsonObject infoObj = docJson.get(OpenApiConstants.INFO).getAsJsonObject();
    final JsonElement apiDescInJson = infoObj.get(OpenApiConstants.DESCRIPTION);
    if (apiDescInJson != null) {
      return apiDescInJson.getAsString();
    }
    return null;
  }
}
