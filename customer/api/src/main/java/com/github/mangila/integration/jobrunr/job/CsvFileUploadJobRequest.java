package com.github.mangila.integration.jobrunr.job;

import org.jobrunr.jobs.lambdas.JobRequest;

public record CsvFileUploadJobRequest(String originalFileName, String path, String domain) implements JobRequest {

    @Override
    public Class<CsvFileUploadJobHandler> getJobRequestHandler() {
        return CsvFileUploadJobHandler.class;
    }
}
