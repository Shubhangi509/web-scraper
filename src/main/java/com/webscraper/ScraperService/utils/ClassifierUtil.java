package com.webscraper.ScraperService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.FetchedData;
import org.springframework.beans.BeansException;

/**
 * Utility class for classifying URLs into page types
 */
@Slf4j
@Component
public class ClassifierUtil {

    private final ApplicationContext applicationContext;

    public ClassifierUtil(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Classifies the type of page based on URL patterns
     * First tries to use a domain-specific classifier, falls back to default if none exists
     * @param fetchedData The fetched data to classify
     * @return The page type (product, category, home, search, other)
     */
    public String classifyPageType(FetchedData fetchedData) {
        // Try to use domain-specific classifier
        String domain = fetchedData.getDomain();
        String url = fetchedData.getUrl();
        String html = fetchedData.getHtml();
        String formattedDomain = domain.replace(".", "_");
        String beanName = formattedDomain + "_classifier";
        
        log.info("Looking for domain-specific classifier with bean name: {}", beanName);
        
        try {
            // Check if bean exists before trying to get it
            if (applicationContext.containsBean(beanName)) {
                ClassifierScript classifier = applicationContext.getBean(beanName, ClassifierScript.class);
                String pageType = classifier.classify(fetchedData);
                log.info("Domain-specific classifier found, classified as: {}", pageType);
                return pageType;
            } else {
                log.info("No domain-specific classifier found for {}, using default classifier", domain);
            }
        } catch (BeansException e) {
            log.warn("Error getting domain-specific classifier for {}: {}", domain, e.getMessage());
        } catch (Exception e) {
            log.warn("Error using domain-specific classifier: {}", e.getMessage());
        }
        
        // Fall back to default classifier
        log.info("Using default classifier for domain: {}", domain);
        try {
            ClassifierScript defaultClassifier = applicationContext.getBean("default_classifier", ClassifierScript.class);
            return defaultClassifier.classify(fetchedData);
        } catch (BeansException e) {
            log.error("Could not find default_classifier bean! This is a critical error: {}", e.getMessage());
            // If even the default classifier is missing, use a simple fallback strategy
            if (url.contains("/product/") || url.contains("/dp/") || url.contains("/p/")) {
                return "product";
            } else if (url.contains("/category/") || url.contains("/c/") || url.contains("/s?")) {
                return "category";
            } else {
                return "other";
            }
        }
    }
} 