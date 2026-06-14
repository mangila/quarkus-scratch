package com.github.mangila.web1.person.web;

import com.github.mangila.web1.person.web.model.PersonDto;
import io.quarkus.cache.CacheKeyGenerator;
import java.lang.reflect.Method;

public class PersonDtoCacheKeyGenerator implements CacheKeyGenerator {

  @Override
  public Object generate(Method method, Object... methodParams) {
    final PersonDto dto = (PersonDto) methodParams[0];
    return dto.id();
  }
}
