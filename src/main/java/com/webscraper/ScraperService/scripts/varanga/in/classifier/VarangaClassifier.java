package com.webscraper.ScraperService.scripts.varanga.in.classifier;

import com.webscraper.ScraperService.utils.ClassifierScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.FetchedData;

/**
 * Classifier for varanga.in pages
 * Identifies product pages and category pages based on URL patterns
 */
@Slf4j
@Component("varanga_in_classifier")
public class VarangaClassifier implements ClassifierScript {

    @Override
    public String classify(FetchedData fetchedData) {
        String url = fetchedData.getUrl();
        log.info("Classifying Varanga URL: {}", url);
        
        // Product page URLs typically contain "/catalogue/" followed by a book slug
        if (url.contains("/products/")) {
            log.info("Classified as product page");
            return "product";
        }
        log.info("Classified as category page");
        return "category";
    }
} 