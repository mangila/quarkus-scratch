package com.github.mangila.crud1.person.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@Path("/favicon.ico")
public class FaviconResource {

  private static final int TWENTY_FOUR_HOURS = 86_400;

  @GET
  public Response favicon() {
    final CacheControl cacheControl = new CacheControl();
    cacheControl.setMaxAge(TWENTY_FOUR_HOURS);
    return Response.noContent().cacheControl(cacheControl).build();
  }
}
