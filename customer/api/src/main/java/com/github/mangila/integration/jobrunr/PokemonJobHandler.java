package com.github.mangila.integration.jobrunr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.integration.pokeapi.PokeApiService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * This job will fetch a Pokémon by id and enrich the customer entity with its favorite Pokémon
 */
@ApplicationScoped
public class PokemonJobHandler implements JobRequestHandler<PokemonJobRequest> {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(PokemonJobHandler.class));

    private final PokeApiService pokeApiService;
    private final CustomerPostgresRepository customerPostgresRepository;

    public PokemonJobHandler(PokeApiService pokeApiService,
                             CustomerPostgresRepository customerPostgresRepository) {
        this.pokeApiService = pokeApiService;
        this.customerPostgresRepository = customerPostgresRepository;
    }

    @Override
    public void run(PokemonJobRequest jobRequest) throws Exception {
        final int pokemonId = jobRequest.pokemonId();
        final UUID customerId = jobRequest.customerId();
        MDC.put("pokemon.id", String.valueOf(pokemonId));
        MDC.put("customer.id", customerId.toString());
        final ObjectNode node = pokeApiService.fetchPokemonById(pokemonId);
        customerPostgresRepository.persistFavoritePokemon(customerId, node);
        log.info("Pokemon id: {} name: {}", pokemonId, node.get("name").asText());
    }
}
