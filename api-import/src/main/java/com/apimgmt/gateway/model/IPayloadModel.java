package com.apimgmt.gateway.model;

public interface IPayloadModel extends Cloneable {
  Object getPaylod();

  String getId();

  String getName();

  Long getLastModified();

}
