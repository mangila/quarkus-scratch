package com.github.mangila.crud1.integration.httpbin;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "httpbin")
public interface HttpBinClient {}
