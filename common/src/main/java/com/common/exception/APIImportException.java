package com.common.exception;


public class APIImportException extends Exception {

  private static final long serialVersionUID = 1L;
  public APIImportException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

}
