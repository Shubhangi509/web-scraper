package com.webscraper.ScraperService.utils;

import com.webscraper.ScraperService.entity.FetchedData;
import org.springframework.stereotype.Component;

/**
 * Interface for domain-specific URL classifiers
 * Implementations should be in scripts/{domain}/classifier/ directory
 */
@Component
public interface ClassifierScript {
    /**
     * Classify a URL into a page type
     * @param url The URL to classify
     * @return The page type (e.g., "product", "category", "search", "home", "other")
     */
    String classify(FetchedData fetchedData);
} 