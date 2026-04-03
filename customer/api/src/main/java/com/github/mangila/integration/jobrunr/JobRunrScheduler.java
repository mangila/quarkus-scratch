package com.github.mangila.integration.jobrunr;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import com.github.mangila.integration.csv.ProductCsvRoute;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    public void schedule(@Positive int pokemonId, @NotNull UUID customerId, Duration delay) {
        final var request = new PokemonJobRequest(pokemonId, customerId);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Pokemon: %s".formatted(pokemonId))
                .withJobRequest(request)
                .withLabels("pokemon")
                .withAmountOfRetries(10);
        scheduler.create(job);
        Log.infof("Scheduled PokemonId: %s for CustomerId: %s", pokemonId, customerId);
    }

    public void schedule(CustomerCsvRoute.CustomerCsv csv, Duration delay) {
        final var request = new CustomerCsvJobRequest(csv);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Customer: %s".formatted(csv.getId()))
                .withJobRequest(request)
                .withLabels("customer", "csv")
                .withAmountOfRetries(10);
        scheduler.create(job);
        Log.info("customer csv");
    }

    public void schedule(ProductCsvRoute.ProductCsv csv, Duration delay) {
        final var request = new ProductCsvJobRequest(csv);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Product: %s".formatted(csv.getId()))
                .withJobRequest(request)
                .withLabels("product", "csv")
                .withAmountOfRetries(10);
        scheduler.create(job);
        Log.info("product csv");
    }
}
