package com.github.mangila.crud1;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestResourceUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  public static String getTestResource(String resourceName) {
    try (InputStream is =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T getTestResourceAs(String resourceName, Class<T> clazz) {
    try (InputStream is =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
      final String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      return MAPPER.readValue(content, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
