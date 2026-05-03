package com.github.mangila.crud1.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@Path("/favicon.ico")
public class FaviconResource {

  @GET
  public Response favicon() {
    final CacheControl cacheControl = new CacheControl();
    cacheControl.setMaxAge(86400);
    return Response.noContent().cacheControl(cacheControl).build();
  }
}
