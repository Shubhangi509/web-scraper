package com.webscraper.ScraperService.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.utils.Fetcher;
import com.webscraper.ScraperService.utils.FileStorageUtil;

@Slf4j
@Service
public class FetchService {

    private FileStorageUtil fileStorageUtil;
    
    private Fetcher fetcher;
    
    private final ApplicationContext applicationContext;

    private static final String BASE_PATH = "/ScraperService/scripts/";

    @Autowired
    public FetchService(Fetcher fetcher, FileStorageUtil fileStorageUtil, 
                       ApplicationContext applicationContext) {
        this.fetcher = fetcher;
        this.fileStorageUtil = fileStorageUtil;
        this.applicationContext = applicationContext;
    }

    public FetchedData fetchPage(String url) throws Exception {
        String domain = getDomain(url);
        log.info("Domain: {}", domain);

        String html = fetcher.plainFetch(url);
        String apiData = "";
        if(!html.isEmpty()) { //plain fetch success
            apiData = apiReplication(html, url, domain, "");
        }
        else {
            html = fetcher.seleniumFetch(url);
             if(html.isEmpty()) {
                 log.info("Selenium Fetcher failed.");
             }
             else {
                 apiData = apiReplication(html, url, domain, "");
             }
        }

        // Create FetchedData object but don't save it to repository
        FetchedData data = new FetchedData();
        data.setDomain(domain);
        data.setUrl(url);
        data.setHtml(html);
        data.setApiData(apiData);
        
        return data;
    }

    private String getDomain(String url) {
        try {
            Pattern pattern = Pattern.compile("https?://(?:www\\.)?([^/:]+)");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String domain = matcher.group(1);
                log.info("Extracted domain: {}", domain);
                return domain;
            }
        } catch (Exception e) {
            log.error("Error extracting domain from URL: {}", e.getMessage());
        }
        
        // Fallback: try to extract domain from the URL manually
        try {
            if (url.startsWith("http")) {
                // Remove protocol
                String noProtocol = url.split("://")[1];
                // Take everything up to first slash
                String domain = noProtocol.split("/")[0];
                // Remove www if present
                if (domain.startsWith("www.")) {
                    domain = domain.substring(4);
                }
                log.info("Fallback domain extraction: {}", domain);
                return domain;
            }
        } catch (Exception e) {
            log.error("Error in fallback domain extraction: {}", e.getMessage());
        }
        
        log.warn("Could not extract domain from URL: {}", url);
        return "";
    }

    private boolean customFetcherPresent(String domain, String pageType) {
        String filePath = BASE_PATH + domain + "/fetcher/" + pageType + "PageFetcher";
        File file = new File(filePath);
        return file.exists();
    }

    private String apiReplication(String html, String url, String domain, String pageType) {
        String apiData = "";
        try {
            if(customFetcherPresent(domain, pageType)) {
                log.info("Custom Fetcher Present");
                log.info("Trying to replicate API");
                apiData = "";
            }
        } catch (Exception e) {
            log.error("API replication unsuccessful with error: {}", e.getMessage());
        }
        return apiData;
    }
}
