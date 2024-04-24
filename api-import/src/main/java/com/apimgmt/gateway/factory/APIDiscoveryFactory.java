package com.apimgmt.gateway.factory;


import com.common.api.gateway.IRepositoryService;
import com.common.exception.OpenDiscoveryException;
import com.common.exception.RepositoryNotFoundException;
import com.common.model.ConnectionModel;
import java.util.Iterator;
import java.util.ServiceLoader;


public class APIDiscoveryFactory {

  /** Transform API to APIProxyData
   * @param repositoryType Repository Type
   * @return IRepositoryService
   * @throws RepositoryNotFoundException
   * @throws OpenDiscoveryException
   */
  public static IRepositoryService getRepositorySystem(
      final String repositoryType) throws RepositoryNotFoundException, OpenDiscoveryException {
    ServiceLoader<IRepositoryService> loader =
        ServiceLoader.load(IRepositoryService.class);
    Iterator<IRepositoryService> it = loader.iterator();
    while (it.hasNext()) {
      IRepositoryService repository = it.next();
      if (repository.getType().equalsIgnoreCase(repositoryType)) {
        repository.initializeService(getConnectionModel(repositoryType));
        return repository;
      }
    }
    throw new RepositoryNotFoundException(
        "Repository : " + repositoryType + " not found");
  }

  private static ConnectionModel getConnectionModel(String connName) {
    return new ConnectionModel(connName);
  }
}

