package com.webscraper.ScraperService.scripts.bookstoscrape.com.classifier;

import com.webscraper.ScraperService.utils.ClassifierScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.FetchedData;

/**
 * Classifier for books.toscrape.com pages
 * Identifies product pages and category pages based on URL patterns
 */
@Slf4j
@Component("books_toscrape_com_classifier")
public class PageClassifier implements ClassifierScript {

    @Override
    public String classify(FetchedData fetchedData) {
        String url = fetchedData.getUrl();
        log.info("Classifying Books To Scrape URL: {}", url);
        
        // Product page URLs typically contain "/catalogue/" followed by a book slug
        if (url.contains("/catalogue/") && !url.contains("/catalogue/page-")) {
            log.info("Classified as product page");
            return "product";
        }
        log.info("Classified as category page");
        return "category";
    }
} 
