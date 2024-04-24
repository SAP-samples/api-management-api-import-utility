package com.apimgmt.gateway.processor;



import com.apimgmt.gateway.connection.APIMConnectionUtility;
import com.apimgmt.gateway.exception.APIManagementHTTPException;
import com.apimgmt.gateway.model.ProductCreateRequest;
import com.apimgmt.gateway.model.ResponseData;
import com.common.SmartDiscoveryExecutor;
import com.common.exception.APIManagementExecutorException;
import com.common.exception.OpenDiscoveryException;
import com.common.model.API;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIManagementService {
  private APIProductProcessor apiProductProcessor;
  private APIProxyProcessor apiProxyProcessor;

  public APIManagementService() {
    this.apiProductProcessor = new APIProductProcessor();
    this.apiProxyProcessor = new APIProxyProcessor();
  }


 /* Create Product in API Management
   *
   * @param apis List of APIs
   * @param req ProductCreateRequest
   * @return ResponseData
   * @throws OpenDiscoveryException
   */
  public ResponseData createProduct(List<API> apis, ProductCreateRequest req)
          throws OpenDiscoveryException {
    try {
      String token = APIMConnectionUtility.getAccessToken();
      SmartDiscoveryExecutor<ResponseData> executor =
              new SmartDiscoveryExecutor<>("api-update", 10);
      List<Supplier<ResponseData>> tasks = new ArrayList<>();
      apis.forEach((api) -> {
        tasks.add(() -> apiProxyProcessor.createUpdateProxy(api, token));
      });
      final List<ResponseData> tasksResponse = executor.executeTasks(tasks);
      log.debug("API update/create Response {}", tasksResponse);
      return apiProductProcessor.createUpdateProduct(req, apis, token);
    } catch (APIManagementHTTPException | OpenDiscoveryException e) {
      throw new OpenDiscoveryException("Error in product creation : ", e);
    } catch (APIManagementExecutorException e) {
      log.trace(" Error in API create/update : ", e);
      log.error(" Error in API create/update : " + e.getMessage());
      ResponseData<List<API>> res = new ResponseData();
      res.setSuccess(false);
      res.setSpec(apis);
      return res;
    }
  }
}
