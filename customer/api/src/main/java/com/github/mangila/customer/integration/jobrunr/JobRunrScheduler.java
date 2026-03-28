package com.github.mangila.customer.integration.jobrunr;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.MDC;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobRequestScheduler;

import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class JobRunrScheduler {

    private final JobRequestScheduler scheduler;

    public JobRunrScheduler(JobRequestScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(int pokemonId, UUID customerId, Duration delay) {
        MDC.put("customerId", customerId.toString());
        MDC.put("pokemonId", Integer.toString(pokemonId));
        MDC.put("delayInSeconds", delay.toSeconds());
        final var request = new PokemonJobRequest(pokemonId, customerId);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Pokemon: %s".formatted(pokemonId))
                .withJobRequest(request)
                .withLabels("pokemon")
                .withAmountOfRetries(10);
        try {
            scheduler.create(job);
            Log.info("Scheduled Pokemon");
        } finally {
            MDC.clear();
        }
    }
}
