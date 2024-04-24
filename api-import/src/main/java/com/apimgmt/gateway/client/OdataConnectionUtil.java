package com.apimgmt.gateway.client;

import com.apimgmt.gateway.model.ResponseData;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Slf4j
public class OdataConnectionUtil {

  public static ResponseData parseResponse(List<BatchResponse> batchResponses) {
    ResponseData<String> res = new ResponseData();
    res.setStatus_code(Integer.valueOf(HttpStatus.SC_ACCEPTED));
    String msg = null;
    for (BatchResponse batch : batchResponses) {
      msg = String.format("Error in entity update : %s  response: %s"
          , batch.getStatus_code(), batch.getResponse());
      if (!isSuccess(Integer.valueOf(batch.getStatus_code()))) {
        res.setStatus_code(Integer.valueOf(batch.getStatus_code()));
        res.setSuccess(false);
        log.error(msg);
      } else {
        res.setSuccess(true);
        msg = String.format("Entity updated successfully");

      }
    }
    res.setSpec(msg);
    return res;
  }

  public static Boolean isSuccess(final int statusCode) {
    return statusCode >= BatchConstants.SUCCESS_MIN
        && statusCode <= BatchConstants.SUCESS_MAX;
  }


  public static CloseableHttpResponse executeHttpMethod(HttpRequestBase httpRequest) {
    try {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      return httpClient.execute(httpRequest);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
