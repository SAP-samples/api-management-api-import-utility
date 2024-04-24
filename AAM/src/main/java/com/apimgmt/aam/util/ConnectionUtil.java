package com.apimgmt.aam.util;


import com.apimgmt.aam.connection.AAMConnectionResolver;
import com.common.ConnectionConstants;
import com.common.api.connection.IConnectivityResolver;
import com.common.exception.ConnectionResolverException;
import com.common.model.ConnectionModel;
import org.apache.http.impl.client.CloseableHttpClient;

public class ConnectionUtil {

  public static CloseableHttpClient getHttpClient(ConnectionModel conn)
          throws ConnectionResolverException {
    final IConnectivityResolver connectivityResolver = getConnectivityResolver(conn);
    return connectivityResolver.getHttpClient(conn.getName());
  }


  public static CloseableHttpClient getCustomHttpClient(ConnectionModel conn)
          throws ConnectionResolverException {
    IConnectivityResolver connService = getConnectivityResolver(conn);
    return connService.getCustomHttpClient(conn.getName(), null);
  }

  public static Boolean isSuccess(final int statusCode) {
    return statusCode >= ConnectionConstants.SUCCESS_MIN
            && statusCode <= ConnectionConstants.SUCESS_MAX;
  }

  private static IConnectivityResolver getConnectivityResolver(ConnectionModel conn) {
    return new AAMConnectionResolver();
  }

}
