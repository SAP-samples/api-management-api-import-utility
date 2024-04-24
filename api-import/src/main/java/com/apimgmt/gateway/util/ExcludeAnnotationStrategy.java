package com.apimgmt.gateway.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExcludeAnnotationStrategy implements ExclusionStrategy {
  @Override
  public boolean shouldSkipClass(final Class<?> clazz) {
    return false;
  }

  @Override
  public boolean shouldSkipField(final FieldAttributes field) {
    return field.getAnnotation(Exclude.class) != null;
  }
}
