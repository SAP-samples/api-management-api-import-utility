package com.apimgmt.gateway.processor;

import com.apimgmt.gateway.factory.APIDiscoveryFactory;
import com.common.api.gateway.IRepositoryService;
import com.common.exception.OpenDiscoveryException;
import com.common.exception.RepositoryNotFoundException;
import com.common.model.API;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class APIDiscoveryService {


  /* Get the list of APIs from the repository
   *
   * @param repositoryName Repository Name
   * @return List of APIs
   * @throws OpenDiscoveryException
   */
  public List<API> getApis(final String repositoryName) throws OpenDiscoveryException {
    try {
      final IRepositoryService discoverySystem = APIDiscoveryFactory
              .getRepositorySystem(repositoryName);
      return discoverySystem.getListOfApis();
    } catch (IllegalArgumentException | RepositoryNotFoundException e) {
      throw new OpenDiscoveryException("Illegal argument request : " + e.getMessage(), e);
    }
  }

}
