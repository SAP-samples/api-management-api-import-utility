package com.apimgmt.gateway.util;

import com.apimgmt.gateway.openapi.APIMSanitizer;
import com.apimgmt.gateway.openapi.APIMSanitizerType;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class TransformerUtil {
  public static String encodeJson(final String content) throws MalformedURLException {
    final String sanitizedDoc =
        APIMSanitizer.sanitizeSwaggerJSONContent(content, APIMSanitizerType.BasicWithImages);
    byte[] encodedDoc = Base64.encodeBase64(sanitizedDoc.getBytes(StandardCharsets.UTF_8));
    String encodedString = new String(encodedDoc, StandardCharsets.UTF_8);
    return encodedString;
  }

  public static String decodeJson(final String content) {
    return new String(Base64.decodeBase64(content), StandardCharsets.UTF_8);
  }
}
