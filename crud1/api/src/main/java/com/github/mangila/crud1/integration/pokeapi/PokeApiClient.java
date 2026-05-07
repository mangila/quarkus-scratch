package com.github.mangila.crud1.integration.pokeapi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "pokeapi")
public interface PokeApiClient {}
