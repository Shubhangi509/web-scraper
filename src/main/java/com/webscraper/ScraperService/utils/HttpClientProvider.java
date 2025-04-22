package com.webscraper.ScraperService.utils;

import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class HttpClientProvider {

    private final HttpClient httpClient;

    public HttpClientProvider() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
