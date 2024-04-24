package com.apimgmt.gateway.comparator;

public class DiffEntity {
  private ModelEntity apisToDelete;
  private ModelEntity apisToCreate;
  private ModelEntity apisToUpdate;

  public DiffEntity(ModelEntity apisToDelete, ModelEntity apisToCreate, ModelEntity apisToUpdate) {
    this.apisToDelete = apisToDelete;
    this.apisToCreate = apisToCreate;
    this.apisToUpdate = apisToUpdate;
  }

  public ModelEntity getEntityToDelete() {
    return apisToDelete;
  }

  public ModelEntity getEntityToCreate() {
    return apisToCreate;
  }

  public ModelEntity getEntityToUpdate() {
    return apisToUpdate;
  }
}