package com.webscraper.ScraperService.scripts.redtape.com.classifier;

import com.webscraper.ScraperService.utils.ClassifierScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.FetchedData;

/**
 * Classifier for redtape.com pages
 * Identifies product pages and category pages based on URL patterns
 */
@Slf4j
@Component("redtape_com_classifier")
public class PageClassifier implements ClassifierScript {

    @Override
    public String classify(FetchedData fetchedData) {
        String url = fetchedData.getUrl();
        log.info("Classifying Red Tape URL: {}", url);

        if (url.contains("/products/")) {
            log.info("Classified as product page");
            return "product";
        }
        if(url.contains("/collections/")) {
            log.info("Classified as category page");
            return "category";
        }
        log.info("Neither product not category page");
        return "";
    }

}
