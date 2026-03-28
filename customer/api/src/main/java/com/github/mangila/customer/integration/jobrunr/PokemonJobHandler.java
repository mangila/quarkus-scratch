package com.github.mangila.customer.integration.jobrunr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.integration.pokeapi.PokeApiRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * This job will fetch a Pokémon by id and enrich the customer entity with its favorite Pokémon
 */
@ApplicationScoped
public class PokemonJobHandler implements JobRequestHandler<PokemonJobRequest> {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(PokemonJobHandler.class));

    private final PokeApiRestClient pokeApiRestClient;
    private final CustomerPostgresRepository customerPostgresRepository;

    public PokemonJobHandler(@RestClient PokeApiRestClient pokeApiRestClient,
                             CustomerPostgresRepository customerPostgresRepository) {
        this.pokeApiRestClient = pokeApiRestClient;
        this.customerPostgresRepository = customerPostgresRepository;
    }

    @Override
    public void run(PokemonJobRequest jobRequest) throws Exception {
        final int pokemonId = jobRequest.pokemonId();
        final UUID customerId = jobRequest.customerId();
        final ObjectNode node = pokeApiRestClient.fetchPokemonById(pokemonId);
        customerPostgresRepository.persistFavoritePokemon(customerId, node);
        log.info("Pokemon id: {} name: {}", pokemonId, node.get("name").asText());
    }
}
