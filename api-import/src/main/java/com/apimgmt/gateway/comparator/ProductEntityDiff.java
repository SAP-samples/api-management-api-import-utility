package com.apimgmt.gateway.comparator;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductEntityDiff {
  private List<String> apisToCreate;
  private List<String> apisToDelete;
}
