package com.common.exception;

public class ConnectionResolverException extends Exception {

  private static final long serialVersionUID = 1L;
  private String errorCode;

  public ConnectionResolverException(String message) {
    super(message);
  }

  public ConnectionResolverException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ConnectionResolverException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ConnectionResolverException(Throwable throwable) {
    super(throwable);
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public static String getAppendedMessage(String message, Object... parameters) {
    if (parameters != null && parameters.length >= 1) {
      StringBuilder appendedMessageText = new StringBuilder(message);
      Object[] var3 = parameters;
      int var4 = parameters.length;

      for (int var5 = 0; var5 < var4; ++var5) {
        Object param = var3[var5];
        appendedMessageText.append(":");
        appendedMessageText.append(param);
      }

      return appendedMessageText.toString();
    } else {
      return message;
    }
  }
}
