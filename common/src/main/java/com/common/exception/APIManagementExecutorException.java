package com.common.exception;


public class APIManagementExecutorException extends Exception {

  private static final long serialVersionUID = 1L;

  public APIManagementExecutorException(final String errorCode) {
    super(errorCode);
  }

  public APIManagementExecutorException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

  public APIManagementExecutorException(final Throwable e) {
    super(e);
  }
}
