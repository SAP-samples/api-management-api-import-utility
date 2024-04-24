package com.apimgmt.gateway.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {

  public static Gson getGson() {
    Gson gson = new GsonBuilder()
      .setExclusionStrategies(new ExcludeAnnotationStrategy())
      .setExclusionStrategies(new LinkAnnotationStrategy())
      .disableHtmlEscaping()
      .create();
    return gson;
  }

  public static Gson getGsonWithLink() {
    Gson gson = new GsonBuilder()
      .setExclusionStrategies(new ExcludeAnnotationStrategy())
      .disableHtmlEscaping()
      .create();
    return gson;
  }
}
