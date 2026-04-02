package com.github.mangila.integration.jobrunr;

import org.jobrunr.jobs.lambdas.JobRequest;

import java.util.UUID;

public record PokemonJobRequest(int pokemonId, UUID customerId) implements JobRequest {

    @Override
    public Class<PokemonJobHandler> getJobRequestHandler() {
        return PokemonJobHandler.class;
    }
}
