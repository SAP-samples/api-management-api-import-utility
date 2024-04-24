package com.apimgmt.gateway.client;

public class BatchConstants {
  public static final String LINKS = "/$links/";
  public static final String ADD_PARAMETER = "('%s')";
  public static final String API_PROXIES = "APIProxies('%s')";
  public static final String API_PRODUCT = "apiProducts";
  public static final String API_PROXY = "apiProxies";
  public static final String ADD_RESOURCES = "APIResources";
  public static final String API_RESOURCES = "APIResources('%s')";
  public static final String ADD_PRODUCTS = "APIProducts(name='%s')";
  public static final String ADD_PROXIES = "APIProxies(name='%s')";
  public static final String PRODUCT_PROXY_LINK = "APIProducts(name='%s')/$links/apiProxies";
  public static final String PROXY_PRODUCT_LINK = "APIProxies(name='%s')/$links/apiProducts";
  public static final String PUT_DOCUMENTATION = "Documentations(id='%s',locale='%s')";
  public static final String PUT_API_PRODUCTS = "APIMgmt.APIProducts('%s')";
  public static final String BATCH_CALL_FAILED = "BATCH CALL FAILED STATUS {0}";
  public static final String API_PROXY_END_POINTS = "APIProxyEndPoints('%s')";
  public static final String PROXY_END_POINTS = "APIProxyEndPoints";

  public static final int SUCCESS_MIN = 200;
  public static final int SUCESS_MAX = 299;
  public static final int MAX_RETRIES = 1;

}
