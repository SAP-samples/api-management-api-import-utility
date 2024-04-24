package com.common.api.gateway;


import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import com.common.model.APIDefinition;
import com.common.model.ConnectionModel;
import java.util.List;

/**
 * Interface for Repository Service
 */
public interface IRepositoryService {
  void initializeService(ConnectionModel model) throws OpenDiscoveryException;

  List<API> getListOfApis() throws OpenDiscoveryException;

  API getApi(String apiName) throws OpenDiscoveryException;

  APIDefinition getApiDefinition(API apiName) throws OpenDiscoveryException;

  String getType();
}
