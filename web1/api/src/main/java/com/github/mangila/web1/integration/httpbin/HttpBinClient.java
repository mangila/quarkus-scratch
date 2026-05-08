package com.github.mangila.web1.integration.httpbin;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "httpbin")
public interface HttpBinClient {}
