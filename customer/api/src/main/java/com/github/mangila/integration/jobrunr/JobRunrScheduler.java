package com.github.mangila.integration.jobrunr;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.jobs.context.JobContext;
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

    @Recurring(id = "my-recurring-job", cron = "*/15 * * * * *")
    @Job(name = "My recurring job")
    public void executeSampleJob(JobContext context) {
        Log.info("Executing sample job");
        context.runStepOnce("hej", () -> Log.info("Hello world"));
        throw new RuntimeException("Test");
    }
}
