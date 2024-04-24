package com.apimgmt.gateway.model;

import java.util.ArrayList;
import java.util.List;

public class ResultModel<T> {
  private List<T> results = new ArrayList<T>();

  public List<T> getResults() {
    return results;
  }

  public void setResults(final ArrayList<T> results) {
    this.results = results;
  }

}
