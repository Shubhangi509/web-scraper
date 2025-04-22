package com.webscraper.ScraperService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.FetchedData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default classifier for URLs when no domain-specific classifier is available
 */
@Slf4j
@Component("default_classifier")
public class DefaultClassifier implements ClassifierScript {

    // Patterns for product pages
    private static final List<Pattern> PRODUCT_PATTERNS = new ArrayList<>();
    
    // Patterns for category pages
    private static final List<Pattern> CATEGORY_PATTERNS = new ArrayList<>();
    
    static {
        // Initialize product page patterns
        PRODUCT_PATTERNS.add(Pattern.compile("/product/", Pattern.CASE_INSENSITIVE));
        PRODUCT_PATTERNS.add(Pattern.compile("/dp/[A-Z0-9]{10}", Pattern.CASE_INSENSITIVE)); // Amazon
        PRODUCT_PATTERNS.add(Pattern.compile("/p/[a-zA-Z0-9]+", Pattern.CASE_INSENSITIVE)); // Flipkart
        PRODUCT_PATTERNS.add(Pattern.compile("/catalogue/[^/]+", Pattern.CASE_INSENSITIVE)); // Books To Scrape
        PRODUCT_PATTERNS.add(Pattern.compile("/products/[^/]+", Pattern.CASE_INSENSITIVE));
        PRODUCT_PATTERNS.add(Pattern.compile("/item/[^/]+", Pattern.CASE_INSENSITIVE));
        PRODUCT_PATTERNS.add(Pattern.compile("productdetails", Pattern.CASE_INSENSITIVE));
        PRODUCT_PATTERNS.add(Pattern.compile("product_id=", Pattern.CASE_INSENSITIVE));
        PRODUCT_PATTERNS.add(Pattern.compile("productId=", Pattern.CASE_INSENSITIVE));
        
        // Initialize category page patterns
        CATEGORY_PATTERNS.add(Pattern.compile("/category/", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("/c/", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("/cat/", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("/catalog/", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("s\\?k=", Pattern.CASE_INSENSITIVE)); // Amazon search
        CATEGORY_PATTERNS.add(Pattern.compile("category_id=", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("categoryId=", Pattern.CASE_INSENSITIVE));
        CATEGORY_PATTERNS.add(Pattern.compile("/search/", Pattern.CASE_INSENSITIVE));
    }

    @Override
    public String classify(FetchedData fetchedData) {
        String url = fetchedData.getUrl();
        log.info("Default classifier classifying URL: {}", url);
        
        // Check against product page patterns
        for (Pattern pattern : PRODUCT_PATTERNS) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                log.info("URL classified as product page based on pattern: {}", pattern.pattern());
                return "product";
            }
        }
        
        // Check against category page patterns
        for (Pattern pattern : CATEGORY_PATTERNS) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                log.info("URL classified as category page based on pattern: {}", pattern.pattern());
                return "category";
            }
        }
        
        // Check for URL patterns that might indicate a search or listing
        if (url.contains("search") || url.contains("listing") || url.contains("page=") || 
            url.contains("index") || url.contains("browse")) {
            log.info("URL classified as category page based on keyword analysis");
            return "category";
        }
        
        // If the URL ends without any specific indicators, it's likely a home page
        String path = getUrlPath(url);
        if (path.isEmpty() || path.equals("/") || path.endsWith("/index.html")) {
            log.info("URL classified as home page");
            return "home";
        }
        
        // Default to 'other' for unrecognized patterns
        log.info("URL classified as other (unrecognized pattern)");
        return "other";
    }
    
    /**
     * Extract just the path part of the URL
     */
    private String getUrlPath(String url) {
        try {
            // Remove protocol and domain part
            if (url.startsWith("http")) {
                String[] parts = url.split("://", 2);
                if (parts.length > 1) {
                    String pathWithDomain = parts[1];
                    int slashIndex = pathWithDomain.indexOf('/', pathWithDomain.indexOf('.') + 1);
                    if (slashIndex != -1) {
                        return pathWithDomain.substring(slashIndex);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting path from URL: {}", e.getMessage());
        }
        return "";
    }
} 