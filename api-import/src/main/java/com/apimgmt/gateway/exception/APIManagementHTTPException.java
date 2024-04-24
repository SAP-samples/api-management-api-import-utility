package com.apimgmt.gateway.exception;


public class APIManagementHTTPException extends Exception {

  private static final long serialVersionUID = 1L;

  public APIManagementHTTPException(final String errorCode) {
    super(errorCode);
  }

  public APIManagementHTTPException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

  public APIManagementHTTPException(final Throwable e) {
    super(e);
  }
}
