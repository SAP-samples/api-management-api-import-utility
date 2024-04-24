package com.common.util;

import com.common.exception.FileReadException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONReader {

  public static <T> T readConfigJson(final String fileName, final Class<T> classOfT) throws FileReadException {
    log.debug("Filename : {} ", fileName);
    Gson gson = new Gson();
    InputStreamReader isReader = null;
    InputStream inputStream = null;
    T config = null;
    try {
      ClassLoader classLoader = JSONReader.class.getClassLoader();
      if (classLoader != null) {
        inputStream = classLoader.getResourceAsStream(fileName);
        isReader = new InputStreamReader(inputStream);
        config = gson.fromJson(isReader, classOfT);
      }
    } catch (Exception e) {
      throw new FileReadException(e);
    } finally {
      if (isReader != null) {
        try {
          isReader.close();
        } catch (IOException e) {
          log.warn("Error while closing the input stream reader", e);
        }
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          log.warn("Error while closing the input stream", e);
        }
      }
    }
    return config;
  }

  public static <T> T readInputJsonFile(final String fileName, final Class<T> classOfT) throws FileReadException {
    FileReader fileReader = null;
    T config = null;
    try {
      File jarPath = new File(JSONReader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      String propertiesPath = jarPath.getParentFile().getAbsolutePath() + "/conf/" + fileName;
      log.debug("FilePath : {} ", propertiesPath);
      Gson gson = new Gson();
      File file = new File(propertiesPath);
      fileReader = new FileReader(file);
      config = gson.fromJson(fileReader, classOfT);
    } catch (FileNotFoundException e) {
      throw new FileReadException("File not found exception", e);
    } catch (JsonSyntaxException e) {
      throw new FileReadException("Invalid Json", e);
    } catch (URISyntaxException e) {
      throw new FileReadException("Invalid URI Syntax", e);
    } finally {
      if (fileReader != null) {
        try {
          fileReader.close();
        } catch (IOException e) {
          log.warn("Error while closing input json file reader.", e);
        }
      }
    }
    return config;
  }

  public static <T> T readConfigFile(String fileName, final Class<T> classOfT) throws FileReadException {
    T config;
    try {
      config = readConfigJson(fileName, classOfT);
    } catch (FileReadException e) {
      config = readInputJsonFile(fileName, classOfT);
    }
    return config;
  }

}
