package com.github.mangila.web1.shared;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@Path("/favicon.ico")
public class FaviconResource {

  private static final int TWENTY_FOUR_HOURS = 86_400;

  /**
   * Handles implicit browser requests for {@code /favicon.ico}.
   *
   * <p>This application does not provide a favicon, but browsers commonly request one
   * automatically. Returning an empty {@code 204 No Content} response prevents those requests from
   * being treated as missing REST endpoints and allows clients to cache the result for a short
   * period.
   *
   * @return an empty cached response indicating that no favicon is available
   */
  @GET
  public Response favicon() {
    final CacheControl cacheControl = new CacheControl();
    cacheControl.setMaxAge(TWENTY_FOUR_HOURS);
    return Response.noContent().cacheControl(cacheControl).build();
  }
}
