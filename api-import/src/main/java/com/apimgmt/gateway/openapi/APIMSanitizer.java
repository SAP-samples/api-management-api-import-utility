package com.apimgmt.gateway.openapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Map.Entry;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class APIMSanitizer {

  private static final String DESCRIPTION = "description";

  public static String sanitizeHTMLContent(final String content, final APIMSanitizerType type) {
    String sanitizedContent = null;
    if (content != null) {
      Safelist profile = getWhitelistType(type);
      profile.addAttributes("a", "target");
      sanitizedContent = Jsoup.clean(content, profile);
    }
    return sanitizedContent;
  }

  public static String sanitizeSwaggerJSONContent(final String jsonString, final APIMSanitizerType type) {
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    return sanitizeJson(jsonElement, type) != null ? sanitizeJson(jsonElement, type).toString() : null;
  }

  private static JsonElement sanitizeJson(final JsonElement jsonElement, final APIMSanitizerType type) {
    if (jsonElement != null && jsonElement.isJsonObject()) {
      JsonObject obj = jsonElement.getAsJsonObject();
      Iterator<Entry<String, JsonElement>> itr = obj.entrySet().iterator();
      while (itr.hasNext()) {
        Entry<String, JsonElement> entry = itr.next();
        if (entry.getValue().isJsonObject()) {
          JsonElement value = sanitizeJson(entry.getValue(), type);
          entry.setValue(value);
        }
        if (entry.getKey().equals(DESCRIPTION)
            && entry.getValue() != null
            && entry.getValue().isJsonPrimitive()) {
          String value = entry.getValue().getAsString();
          String sanitized = sanitizeHTMLContent(value, type);
          JsonPrimitive jsonPrimitive = new JsonPrimitive(sanitized);
          entry.setValue(jsonPrimitive);
        }
      }
    }
    return jsonElement;
  }

  private static Safelist getWhitelistType(final APIMSanitizerType type) {
    Safelist whitelist = Safelist.simpleText();
    if (type != null) {
      switch (type) {
        case BasicWithImages:
          whitelist = Safelist.basicWithImages();
          break;
        default:
          whitelist = Safelist.simpleText();
          break;
      }
    }
    return whitelist;
  }
}
