package com.common.api.connection;



import com.common.exception.ConnectionResolverException;
import com.common.model.ConnectionProperties;
import org.apache.http.impl.client.CloseableHttpClient;

public interface IConnectivityResolver {

  CloseableHttpClient getHttpClient(String connectionName) throws ConnectionResolverException;

  CloseableHttpClient getCustomHttpClient(String connectionName, ConnectionProperties custom)
      throws ConnectionResolverException;

  String getType();
}
