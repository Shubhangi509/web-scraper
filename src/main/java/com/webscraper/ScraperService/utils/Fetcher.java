package com.webscraper.ScraperService.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import com.webscraper.ScraperService.service.SeleniumDriverManager;
import com.webscraper.ScraperService.service.SeleniumFetcher;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.log.BaseLogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Fetcher {

    private String userAgent;

    private final HttpClient httpClient;
    private SeleniumFetcher seleniumFetcher;
    // private Logger logger = LoggerFactory

    @Autowired
    public Fetcher(Constants constants,
                   HttpClientProvider httpClientProvider,
                   SeleniumFetcher seleniumFetcher) {
        log.info("Constructor called for class Fetcher");
        this.userAgent = constants.getUserAgent();
        this.httpClient = httpClientProvider.getHttpClient();
        this.seleniumFetcher = seleniumFetcher;
    }

    public String plainFetch(String url) throws Exception {
        try {
            log.info("Trying plain fetch for url: {}", url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("user-agent",userAgent)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String html = "";
            if(!ErrorPage.isErrorPage(response)) {
                log.info("Plain fetch successful");
                html = response.body();
            }
            else {
                log.info("Plain fetch unsuccessful");
            }
            return html;
        } catch (Exception e) {
            log.error("Error occurred during plain fetch: {}", e.getMessage());
            throw new Exception(e);
        }
    }

    public String seleniumFetch(String url) throws Exception {
        log.info("Initiating selenium call for url: {}", url);
        String pageSource = seleniumFetcher.executeFetch(url);
        log.info("Navigated successfully");
        return pageSource;
    }
}
