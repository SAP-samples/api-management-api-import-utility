package com.apimgmt.gateway.client;


public class BatchResponse {

  private String status_code;

  private String status_info;

  private String response;

  public String getStatus_code() {
    return status_code;
  }

  public void setStatus_code(final String statusCode) {
    this.status_code = statusCode;
  }

  public String getStatus_info() {
    return status_info;
  }

  public void setStatus_info(final String statusInfo) {
    this.status_info = statusInfo;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(final String response) {
    this.response = response;
  }
}

