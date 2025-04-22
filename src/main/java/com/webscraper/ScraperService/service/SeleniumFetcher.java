package com.webscraper.ScraperService.service;

import com.webscraper.ScraperService.domHandler.DomHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
public class SeleniumFetcher {

    private SeleniumDriverManager seleniumDriverManager;
    private ApplicationContext applicationContext;
    private WebDriver webDriver;

    @Autowired
    public SeleniumFetcher(SeleniumDriverManager seleniumDriverManager, ApplicationContext applicationContext) {
        this.seleniumDriverManager = seleniumDriverManager;
        this.applicationContext = applicationContext;
        this.webDriver = seleniumDriverManager.getWebDriver();
    }

    public String executeFetch(String url) throws Exception {
        log.info("Starting selenium fetch for url: {}", url);
        try {
            webDriver = seleniumDriverManager.manageDriverLifecycle();
            webDriver.navigate().to(url);
            String pageSource = webDriver.getPageSource();
            String pageSourcePostDomHandle = handleDomIfHandlerIsPresent(url, pageSource);
            pageSource = pageSourcePostDomHandle != null ? pageSourcePostDomHandle : pageSource;
            return pageSource;
        } catch (Exception e) {
            log.error("Error occurred while fetching via selenium driver: {}", e.getMessage());
            throw new Exception(e);
        }
    }

    private String handleDomIfHandlerIsPresent(String url, String initialPageSource) {
        String pageSource = null;
        String domain = getDomainFromUrl(url);
        log.info("Domain extracted: {}", domain);
        if(domain != null) {
            try {
                DomHandler domHandler = applicationContext.getBean(domain, DomHandler.class);
                pageSource = domHandler.processPageContent(webDriver, url, initialPageSource);
            } catch (Exception e) {
                log.error("No dom handler present for domain {}: {}", domain, e.getMessage());
            }
        }
        return pageSource;
    }

    private String getDomainFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if(domain != null) {
                if(domain.startsWith("www.")) domain = domain.substring(4);
                if(!domain.isEmpty() && domain.contains(".")) domain = domain.replace(".","_");
                return domain;
            }
        } catch(URISyntaxException e) {
            log.error("Error occurred while getting domain for url: {}", url);
        }
        return null;
    }


}