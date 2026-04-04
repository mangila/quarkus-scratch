package com.github.mangila.integration.jobrunr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.integration.pgcache.TtlCacheRepository;
import com.github.mangila.shared.JsonService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.Job;
import org.jobrunr.storage.StorageProvider;

import java.util.UUID;

@ApplicationScoped
public class JobRunrService {

    private final JsonService jsonService;
    private final TtlCacheRepository ttlCacheRepository;
    private final StorageProvider storageProvider;

    public JobRunrService(JsonService jsonService,
                          TtlCacheRepository ttlCacheRepository,
                          StorageProvider storageProvider) {
        this.jsonService = jsonService;
        this.ttlCacheRepository = ttlCacheRepository;
        this.storageProvider = storageProvider;
    }

    public ObjectNode getJobStatus(UUID id) {
        final Job job = storageProvider.getJobById(id);
        final var state = job.getJobState().getName();
        final var errors = job.getMetadata().getOrDefault("errors", "");
        var node = jsonService.createObjectNode();
        node.put("id", id.toString());
        node.put("state", state.toString());
        node.put("errors", errors.toString());
        return node;
    }
}
