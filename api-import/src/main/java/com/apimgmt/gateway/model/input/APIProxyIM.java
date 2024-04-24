package com.apimgmt.gateway.model.input;


import com.apimgmt.gateway.model.EntityGenericClass;
import com.apimgmt.gateway.model.IPayloadModel;

public class APIProxyIM extends EntityGenericClass implements IPayloadModel {

  private String name;
  private String title;
  private String payload;

  @Override
  public String getName() {
    return title;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setPaylod(final String data) {
    payload = data;
  }

  @Override
  public String getPaylod() {
    return payload;
  }

  @Override
  public String getId() {
    return name;
  }

  @Override
  public Long getLastModified() {
    return getChangedAt();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

}
