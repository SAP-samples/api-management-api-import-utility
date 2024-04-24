package com.apimgmt.gateway.comparator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ProductEntityComparator {

  public static ProductEntityDiff compareEntity(List<String> srcProxies,
                                                List<String> dstProxies) {
    List<String> apisToDelete = new ArrayList<>();
    List<String> apisToCreate = new ArrayList<>();
    Set<String> apisToCreateMap = new HashSet<>();

    for (String api : srcProxies) {
      apisToCreateMap.add(api);
    }

    for (String api : dstProxies) {
      if (apisToCreateMap.contains(api)) {
        apisToCreateMap.remove(api);
      } else {
        apisToDelete.add(api);
      }
    }
    apisToCreate.addAll(apisToCreateMap);
    log.debug("APIs to create {} , delete {} ", apisToCreate, apisToDelete);
    return new ProductEntityDiff(apisToCreate, apisToDelete);
  }
}

