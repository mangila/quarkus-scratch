package com.github.mangila.integration.jobrunr;

import org.jobrunr.jobs.lambdas.JobRequest;

public record CsvUploadJobRequest(String originalFileName, String path, String domain) implements JobRequest {

    @Override
    public Class<CsvUploadJobHandler> getJobRequestHandler() {
        return CsvUploadJobHandler.class;
    }
}
