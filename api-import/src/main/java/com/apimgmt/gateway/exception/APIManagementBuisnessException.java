package com.apimgmt.gateway.exception;


public class APIManagementBuisnessException extends Exception {

  private static final long serialVersionUID = 1L;

  public APIManagementBuisnessException(final String errorCode) {
    super(errorCode);
  }

  public APIManagementBuisnessException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

  public APIManagementBuisnessException(final Throwable e) {
    super(e);
  }
}
