package com.common.exception;


public class OpenDiscoveryException extends Exception {

  private static final long serialVersionUID = 1L;

  public OpenDiscoveryException(final String errorCode) {
    super(errorCode);
  }

  public OpenDiscoveryException(final String errorCode, final Throwable e) {
    super(errorCode, e);
  }

  public OpenDiscoveryException(final Throwable e) {
    super(e);
  }
}
