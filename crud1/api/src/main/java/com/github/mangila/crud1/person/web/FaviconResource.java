package com.github.mangila.crud1.person.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@Path("/favicon.ico")
public class FaviconResource {

  private static final int _24_HOURS = 86400;

  @GET
  public Response favicon() {
    final CacheControl cacheControl = new CacheControl();
    cacheControl.setMaxAge(_24_HOURS);
    return Response.noContent().cacheControl(cacheControl).build();
  }
}
