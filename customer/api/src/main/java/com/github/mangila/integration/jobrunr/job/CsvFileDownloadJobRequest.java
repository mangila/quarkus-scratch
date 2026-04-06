package com.github.mangila.integration.jobrunr.job;

import org.jobrunr.jobs.lambdas.JobRequest;

public record CsvFileDownloadJobRequest(String domain) implements JobRequest {

    @Override
    public Class<CsvFileDownloadJobHandler> getJobRequestHandler() {
        return CsvFileDownloadJobHandler.class;
    }
}
