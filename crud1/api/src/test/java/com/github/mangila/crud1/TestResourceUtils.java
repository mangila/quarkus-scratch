package com.github.mangila.crud1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestResourceUtils {

  public static String getTestResource(String resourceName) {
    try (InputStream is =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
