package com.apimgmt.gateway.client;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Utilities for common converters like inputstream to byte[], to string
 */
public final class StreamUtility {

  private static final int MAX_SIZE = 2048;
  private static final String INDENT_YES = "yes";
  private static final String UTF_8 = "UTF-8";

  private StreamUtility() {

  }

  /**
   * Converts the Inputstream to byte array
   *
   * @param stream
   * @return
   * @throws IOException
   */
  public static byte[] toByteArray(final InputStream stream) throws IOException {
    return toByteArray(stream, MAX_SIZE);
  }

  /**
   * Converts the Inputstream to byte array
   *
   * @param stream
   * @return
   * @throws IOException
   */
  public static byte[] toByteArray(final InputStream stream, final int bufferSize)
      throws IOException {
    ByteArrayOutputStream byteStream = null;
    byte[] data = null;
    try {
      byteStream = new ByteArrayOutputStream();
      final InputStream input = stream;
      byte[] buffer = new byte[bufferSize];
      int size;
      while ((size = input.read(buffer, 0, buffer.length)) != -1) {
        byteStream.write(buffer, 0, size);
      }
      data = byteStream.toByteArray();
    } finally {
      StreamUtility.close(stream);
      StreamUtility.close(byteStream);
    }
    return data;
  }

  /**
   * Converts the ZipInputStream to String data. It would take care of closing the zip entry &
   * the input stream
   *
   * @param zipInput
   * @return
   * @throws IOException
   */
  public static String toString(final ZipInputStream zipInput) throws IOException {
    ByteArrayOutputStream outStream = null;
    String result = null;
    try {
      outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[MAX_SIZE];
      int size;
      while ((size = zipInput.read(buffer, 0, buffer.length)) != -1) {
        outStream.write(buffer, 0, size);
      }
      result = outStream.toString(StandardCharsets.UTF_8.name());
    } finally {
      close(outStream);
      zipInput.closeEntry();
    }
    return result;
  }

  /**
   * Converts the ZipInputStream to String data. It would take care of closing the zip entry &
   * the input stream
   *
   * @return
   * @throws IOException
   */
  public static String toString(final InputStream inputStream) throws IOException {
    ByteArrayOutputStream outStream = null;
    String result = null;
    try {
      outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[MAX_SIZE];
      int size;
      while ((size = inputStream.read(buffer, 0, buffer.length)) != -1) {
        outStream.write(buffer, 0, size);
      }
      result = outStream.toString(StandardCharsets.UTF_8.name());
    } finally {
      close(outStream);
      inputStream.close();
    }
    return result;
  }

  /**
   * This method will take an object of type javax.xml.tansform.Source and return the outer XML in string format
   *
   * @return String with XML content
   * @throws XMLStreamException
   * @throws TransformerException
   */
  public static String toXmlString(final Source source) throws TransformerException {
    StringWriter writer = new StringWriter();
    StreamResult xmlString = new StreamResult(writer);
    Transformer transformer;
    try {
      TransformerFactory factory = TransformerFactory.newInstance();
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8);
      transformer.setOutputProperty(OutputKeys.INDENT, INDENT_YES);
      transformer.transform(source, xmlString);
      writer.flush();
    } finally {
      StreamUtility.close(writer);
    }
    return writer.toString();
  }

  /**
   * Utility method to close any stream,writer,reader item
   *
   * @param item
   * @throws IOException
   */
  public static void close(final Closeable item) {
    if (item != null) {
      try {
        item.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void close(final XMLStreamReader item) {
    if (item != null) {
      try {
        item.close();
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }
  }
}
