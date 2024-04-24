package com.apimgmt.gateway.comparator;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EntityComparator {

  public static DiffEntity compareEntity(List<? extends ModelEntity> src,
                                         List<? extends ModelEntity> dest) {
    ModelEntity entitiesToDelete = null;
    ModelEntity entitiesToUpdate = null;
    ModelEntity entitiesToAdd = null;

    ModelEntity destEntity = null;
    ModelEntity srcEntity = null;

    if (dest == null && src == null) {
      return null;
    }
    if (dest != null && !dest.isEmpty()) {
      destEntity = dest.get(0);
    }
    if (src != null && !src.isEmpty()) {
      srcEntity = src.get(0);
    }
    if (destEntity == null && srcEntity != null) {
      // add entity
      entitiesToAdd = srcEntity;
    }
    if (srcEntity == null && destEntity != null) {
      //delete entity
      entitiesToDelete = destEntity;
    }
    if (srcEntity != null && destEntity != null) {
      if (!srcEntity.equals(destEntity)) {
        // update Entity
        entitiesToUpdate = srcEntity;
      }
    }
    log.debug("Entities to delete {} , add {} , update {}", entitiesToDelete, entitiesToAdd, entitiesToUpdate);
    return new DiffEntity(entitiesToDelete, entitiesToAdd, entitiesToUpdate);
  }
}
