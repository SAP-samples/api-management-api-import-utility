package com.apimgmt.aam.exception;


public class AAMManagementBusinessException extends Exception {

  private static final long serialVersionUID = 1L;

  public AAMManagementBusinessException(final String errorCode) {
    super(errorCode);
  }

  public AAMManagementBusinessException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

  public AAMManagementBusinessException(final Throwable e) {
    super(e);
  }
}
