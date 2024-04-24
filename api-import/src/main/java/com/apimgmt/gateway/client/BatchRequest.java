package com.apimgmt.gateway.client;

import lombok.ToString;

@ToString
public final class BatchRequest {

  private String uri;

  private HttpMethod method;

  private String body;

  public BatchRequest() { }

  public BatchRequest(final String uri, final HttpMethod method, final String body) {
    this.uri = uri;
    this.method = method;
    this.body = body;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(final HttpMethod method) {
    this.method = method;
  }

  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }
}
