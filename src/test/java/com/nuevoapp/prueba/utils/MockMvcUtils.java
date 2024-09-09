package com.nuevoapp.prueba.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public final class MockMvcUtils {
  private static MappingJackson2HttpMessageConverter jacksonMessageConverter;

  private MockMvcUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static MappingJackson2HttpMessageConverter getJacksonMessageConverterInstance() {
    if (jacksonMessageConverter == null) {
      jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
      jacksonMessageConverter.setObjectMapper(
          new ObjectMapper()
              .setSerializationInclusion(JsonInclude.Include.NON_NULL)
              .setDateFormat(new StdDateFormat())
              .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
              .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
              .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
              .enable(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS)
              .registerModule(new JavaTimeModule()));
    }
    return jacksonMessageConverter;
  }
}
