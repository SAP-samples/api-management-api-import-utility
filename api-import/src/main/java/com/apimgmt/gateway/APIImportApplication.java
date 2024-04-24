package com.apimgmt.gateway;

import com.apimgmt.gateway.model.ProductCreateRequest;
import com.apimgmt.gateway.model.ProductRepositoryMap;
import com.apimgmt.gateway.processor.APIDiscoveryService;
import com.apimgmt.gateway.processor.APIManagementService;
import com.common.exception.APIImportException;
import com.common.exception.FileReadException;
import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import com.common.util.JSONReader;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIImportApplication {

  /** Main method to start the API Import process
   * @param args Arguments
   * @throws APIImportException
   */
  public static void main(String[] args) throws APIImportException {
    ProductRepositoryMap[] productRepositoryMaps;
    try {
      productRepositoryMaps = readProductRepositoryMapping("SAP_product_mapping.json");
      if (productRepositoryMaps == null || productRepositoryMaps.length == 0) {
        log.info("No repository is defined to be discovered , define the repository in SAP_product_mapping.json file!");
        return;
      }
    } catch (FileReadException e) {
      throw new APIImportException("Error in SAP product mapping: " + e.getMessage(), e);
    }
    for (ProductRepositoryMap map : productRepositoryMaps) {
      try {
        log.info("Started API discovery for {}",
          map.getRepositoryName());
        APIDiscoveryService apiDiscoveryService = new APIDiscoveryService();
        List<API> apis = apiDiscoveryService.getApis(map.getRepositoryName());
        ProductCreateRequest req = getProductRequest(map.getRepositoryName(), map.getProductFileName());
        APIManagementService productCreateService = new APIManagementService();
        productCreateService.createProduct(apis, req);
        log.info("Completed API discovery");
      } catch (OpenDiscoveryException | IOException e) {
        log.trace("Error in API discovery", e);
        throw new APIImportException("Error in API discovery : " + e.getMessage(), e);
      }
    }
  }

  /**
    Get Product Request
   * @param repositoryName Repository Name
   * @param productFileName Product File Name
   * @return ProductCreateRequest
   * @throws IOException
   */
  private static ProductCreateRequest getProductRequest(String repositoryName, String productFileName)
    throws IOException {
    try {
      log.info("Read {} product details", repositoryName);
      return readProductProperty(productFileName, repositoryName);
    } catch (Exception e) {
      log.error(
        "Failed to load product details for repository {} : falling back to default product details", repositoryName);
      String productName = repositoryName + "ProductName";
      String productTitle = repositoryName + "ProductTitle";
      String productShortText = repositoryName + "ShortText";
      String productDescription = repositoryName + "LongDescription";
      return new ProductCreateRequest(repositoryName, productName, productTitle, productShortText,
        productDescription);
    }
  }

  private static ProductCreateRequest readProductProperty(String fileName, String repositoryName)
    throws FileReadException {
    ProductCreateRequest request = JSONReader.readConfigFile(fileName, ProductCreateRequest.class);
    request.setRepositoryName(repositoryName);
    return request;
  }

  private static ProductRepositoryMap[] readProductRepositoryMapping(String fileName)
    throws FileReadException {
    ProductRepositoryMap[] request = JSONReader.readConfigFile(fileName, ProductRepositoryMap[].class);
    return request;
  }

}
