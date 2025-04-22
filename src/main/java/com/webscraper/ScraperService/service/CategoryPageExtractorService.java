package com.webscraper.ScraperService.service;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.entity.CategoryData;
import com.webscraper.ScraperService.utils.ProductExtractorScript;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Generic service to handle category page extraction for any e-commerce site
 */
@Slf4j
@Service
public class CategoryPageExtractorService {

    private final FetchService fetchService;
    private final ApplicationContext applicationContext;
    
    // Selectors for different e-commerce sites
    private static final Map<String, CategorySelectors> SITE_SELECTORS = new HashMap<>();
    
    // static {
    //     // Configure selectors for Amazon
    //     CategorySelectors amazonSelectors = new CategorySelectors();
    //     amazonSelectors.setProductSelectors(List.of(
    //         "div[data-asin]:not([data-asin='']) a.a-link-normal.s-no-outline", 
    //         "a.a-link-normal[href*='/dp/']"
    //     ));
    //     amazonSelectors.setNextPageSelector("ul.a-pagination li.a-last a");
    //     amazonSelectors.setBaseUrl("https://www.amazon.in");
    //     amazonSelectors.setProductLinkPattern(Pattern.compile("/dp/([A-Z0-9]{10})"));
    //     SITE_SELECTORS.put("amazon.in", amazonSelectors);
        
    //     // Configure selectors for Flipkart
    //     CategorySelectors flipkartSelectors = new CategorySelectors();
    //     flipkartSelectors.setProductSelectors(List.of(
    //         "a[href*='/p/']", 
    //         "div._1AtVbE a[href*='/p/']"
    //     ));
    //     flipkartSelectors.setNextPageSelector("a._1LKTO3");
    //     flipkartSelectors.setBaseUrl("https://www.flipkart.com");
    //     flipkartSelectors.setProductLinkPattern(Pattern.compile("/p/([a-zA-Z0-9]+)"));
    //     SITE_SELECTORS.put("flipkart.com", flipkartSelectors);
        
    //     // Configure selectors for Books To Scrape
    //     CategorySelectors booksToScrapeSelectors = new CategorySelectors();
    //     booksToScrapeSelectors.setProductSelectors(List.of(
    //         "article.product_pod h3 a",
    //         "ol.row li article.product_pod h3 a"
    //     ));
    //     booksToScrapeSelectors.setNextPageSelector("li.next a");
    //     booksToScrapeSelectors.setBaseUrl("https://books.toscrape.com");
    //     booksToScrapeSelectors.setProductLinkPattern(Pattern.compile("/catalogue/([^/]+)"));
    //     SITE_SELECTORS.put("books.toscrape.com", booksToScrapeSelectors);
        
    //     // Add more e-commerce sites as needed
    // }

    @Autowired
    public CategoryPageExtractorService(FetchService fetchService, ApplicationContext applicationContext) {
        this.fetchService = fetchService;
        this.applicationContext = applicationContext;
    }

    /**
     * Process all products from a category page
     * @param categoryUrl The category page URL
     * @param domain The domain (e.g., amazon.in, flipkart.com)
     * @return List of scraped data for all products in the category
     */
    public List<ScrapedData> processAllProductsFromCategory(String categoryUrl, String domain) {
        // log.info("Processing category page for domain {}: {}", domain, categoryUrl);
        // List<ScrapedData> results = new ArrayList<>();
        
        // try {
        //     // Get selectors for this domain or use default generic selectors
        //     CategorySelectors selectors = SITE_SELECTORS.getOrDefault(domain, getDefaultSelectors());
        //     log.info("Using selectors for domain: {} (found: {})", domain, SITE_SELECTORS.containsKey(domain));
            
        //     // Create storage for product URLs and processed URLs
        //     Set<String> pendingProductUrls = new HashSet<>();
        //     Set<String> processedUrls = new HashSet<>();
            
        //     // Create a temp file to store URLs (useful for very large categories)
        //     Path tempUrlFile = Files.createTempFile("category_scraper_", ".urls");
        //     BufferedWriter urlWriter = new BufferedWriter(new FileWriter(tempUrlFile.toFile(), true));
            
        //     // Start with the initial category URL
        //     String currentCategoryUrl = categoryUrl;
        //     boolean hasMorePages = true;
            
        //     // Process all category pages first
        //     while (hasMorePages && currentCategoryUrl != null && !currentCategoryUrl.isEmpty()) {
        //         log.info("Fetching category page: {}", currentCategoryUrl);
                
        //         // Skip if we've already processed this URL
        //         if (processedUrls.contains(currentCategoryUrl)) {
        //             log.info("Category page already processed, skipping: {}", currentCategoryUrl);
        //             break;
        //         }
                
        //         // Fetch the category page
        //         FetchedData categoryFetchedData = fetchService.fetchPage(currentCategoryUrl);
        //         String html = categoryFetchedData.getHtml();
                
        //         // Mark as processed
        //         processedUrls.add(currentCategoryUrl);
                
        //         // Extract product URLs
        //         List<String> productUrlsFromPage = extractProductUrls(
        //             html, 
        //             selectors.getProductSelectors(),
        //             selectors.getBaseUrl(),
        //             selectors.getProductLinkPattern()
        //         );
                
        //         log.info("Found {} product URLs on page {}", productUrlsFromPage.size(), currentCategoryUrl);
                
        //         if (productUrlsFromPage.isEmpty()) {
        //             log.warn("No product URLs found on page. Selectors may not be matching. HTML sample: {}", 
        //                     html.length() > 200 ? html.substring(0, 200) + "..." : html);
        //         }
                
        //         // Add to pending URLs and write to file
        //         for (String url : productUrlsFromPage) {
        //             if (!pendingProductUrls.contains(url) && !processedUrls.contains(url)) {
        //                 pendingProductUrls.add(url);
        //                 urlWriter.write(url);
        //                 urlWriter.newLine();
        //             }
        //         }
        //         urlWriter.flush();
                
        //         // Get next page URL
        //         String nextPageUrl = extractNextPageUrl(
        //             html, 
        //             selectors.getNextPageSelector(),
        //             selectors.getBaseUrl(),
        //             currentCategoryUrl
        //         );
                
        //         if (nextPageUrl != null && !nextPageUrl.isEmpty()) {
        //             log.info("Found next page URL: {}", nextPageUrl);
        //         }
                
        //         // Move to next page or stop if no more pages
        //         if (nextPageUrl != null && !nextPageUrl.isEmpty() && !processedUrls.contains(nextPageUrl)) {
        //             currentCategoryUrl = nextPageUrl;
        //         } else {
        //             hasMorePages = false;
        //         }
        //     }
            
        //     // Close the URL writer now that we've gathered all product URLs
        //     urlWriter.close();
            
        //     log.info("Finished processing category pages. Found {} unique product URLs", pendingProductUrls.size());
            
        //     // Now process each product URL - use a thread pool for parallel processing
        //     ExecutorService executor = Executors.newFixedThreadPool(5);
        //     List<CompletableFuture<ScrapedData>> futures = new ArrayList<>();
            
        //     // Process each product URL
        //     for (String productUrl : pendingProductUrls) {
        //         CompletableFuture<ScrapedData> future = CompletableFuture.supplyAsync(() -> {
        //             try {
        //                 log.info("Processing product URL: {}", productUrl);
                        
        //                 // Fetch the product page
        //                 FetchedData productFetchedData = fetchService.fetchPage(productUrl);
        //                 CategoryData productData = new CategoryData(productFetchedData.getApiData(), productFetchedData.getHtml());
                        
        //                 // Get appropriate product extractor
        //                 String formattedDomain = domain.replace(".", "_");
        //                 String beanName = formattedDomain + "_product";
                        
        //                 if (applicationContext.containsBean(beanName)) {
        //                     ProductExtractorScript productExtractor = applicationContext.getBean(beanName, ProductExtractorScript.class);
        //                     return productExtractor.extract(productFetchedData);
        //                 } else {
        //                     log.warn("No product extractor found for domain {}. Using generic extraction.", domain);
        //                     // Use generic extraction if no specific extractor is found
        //                     return new ScrapedData(domain, productUrl, "product", "unknown", 
        //                         extractTitle(productData.getHtml()), 
        //                         extractDescription(productData.getHtml()),
        //                         0.0, 0.0, 0.0);
        //                 }
        //             } catch (Exception e) {
        //                 log.error("Error processing product {}: {}", productUrl, e.getMessage());
        //                 ScrapedData errorData = new ScrapedData();
        //                 errorData.setDomain(domain);
        //                 errorData.setUrl(productUrl);
        //                 errorData.setPageType("product");
        //                 errorData.setTitle("Error processing product");
        //                 errorData.setDescription(e.getMessage());
        //                 return errorData;
        //             }
        //         }, executor);
                
        //         futures.add(future);
        //     }
            
        //     // Wait for all futures to complete and collect results
        //     for (CompletableFuture<ScrapedData> future : futures) {
        //         results.add(future.join());
        //     }
            
        //     executor.shutdown();
            
        //     // Clean up the temp file
        //     Files.deleteIfExists(tempUrlFile);
            
        // } catch (Exception e) {
        //     log.error("Error processing category products: {}", e.getMessage(), e);
        // }
        
        // return results;
        return null;
    }

    /**
     * Extract product URLs from HTML using selectors
     */
    private List<String> extractProductUrls(String html, List<String> selectors, String baseUrl, Pattern productPattern) {
        // List<String> urls = new ArrayList<>();
        
        // try {
        //     Document doc = Jsoup.parse(html);
            
        //     // Try each selector in order
        //     for (String selector : selectors) {
        //         Elements links = doc.select(selector);
        //         log.debug("Selector {} found {} links", selector, links.size());
                
        //         if (!links.isEmpty()) {
        //             for (Element link : links) {
        //                 String href = link.attr("href");
        //                 if (!href.isEmpty()) {
        //                     // Make sure URL is absolute
        //                     if (href.startsWith("/")) {
        //                         href = baseUrl + href;
        //                     } else if (!href.startsWith("http")) {
        //                         // Handle relative links that don't start with /
        //                         String currentPagePath = baseUrl;
        //                         if (currentPagePath.endsWith("/")) {
        //                             href = currentPagePath + href;
        //                         } else {
        //                             href = currentPagePath + "/" + href;
        //                         }
        //                     }
                            
        //                     // Verify that this is a product URL using the pattern
        //                     if (productPattern == null || productPattern.matcher(href).find()) {
        //                         urls.add(href);
        //                     }
        //                 }
        //             }
                    
        //             // If we found URLs with this selector, no need to try others
        //             if (!urls.isEmpty()) {
        //                 break;
        //             }
        //         }
        //     }
            
        // } catch (Exception e) {
        //     log.error("Error extracting product URLs: {}", e.getMessage());
        // }
        
        // return urls;
        return null;
    }

    /**
     * Extract next page URL
     */
    private String extractNextPageUrl(String html, String nextPageSelector, String baseUrl, String currentUrl) {
        // try {
        //     Document doc = Jsoup.parse(html);
            
        //     // Find next page link
        //     Element nextLink = doc.select(nextPageSelector).first();
        //     if (nextLink != null) {
        //         String href = nextLink.attr("href");
        //         if (!href.isEmpty()) {
        //             // Make sure URL is absolute
        //             if (href.startsWith("/")) {
        //                 return baseUrl + href;
        //             } else if (!href.startsWith("http")) {
        //                 // Handle relative links that don't start with /
        //                 // Get the directory part of current URL
        //                 int lastSlashIndex = currentUrl.lastIndexOf('/');
        //                 if (lastSlashIndex > 0) {
        //                     String currentPageDirectory = currentUrl.substring(0, lastSlashIndex + 1);
        //                     return currentPageDirectory + href;
        //                 } else {
        //                     return baseUrl + "/" + href;
        //                 }
        //             }
        //             return href;
        //         }
        //     }
        // } catch (Exception e) {
        //     log.error("Error extracting next page URL: {}", e.getMessage());
        // }
        return null;
    }
    
    /**
     * Extract title from generic product page
     */
    private String extractTitle(String html) {
        // try {
        //     Document doc = Jsoup.parse(html);
        //     String title = doc.select("h1").first().text().trim();
        //     if (title.isEmpty()) {
        //         title = doc.title();
        //     }
        //     return title;
        // } catch (Exception e) {
        //     return "Unknown Product";
        // }
        return null;
    }
    
    /**
     * Extract description from generic product page
     */
    private String extractDescription(String html) {
        try {
            Document doc = Jsoup.parse(html);
            String description = doc.select("meta[name=description]").attr("content");
            if (description.isEmpty()) {
                description = doc.select("div#description, div.description, div#productDescription").text();
            }
            return description;
        } catch (Exception e) {
            return "No description available";
        }
    }
    
    /**
     * Get default selectors for unknown sites
     */
    private CategorySelectors getDefaultSelectors() {
        CategorySelectors defaultSelectors = new CategorySelectors();
        defaultSelectors.setProductSelectors(List.of(
            "a[href*='/product/']",
            "a[href*='/p/']",
            "a[href*='/dp/']",
            "div.product a",
            "div.item a"
        ));
        defaultSelectors.setNextPageSelector("a.next, a[rel=next], a:contains(Next), a:contains(»)");
        defaultSelectors.setBaseUrl("");
        defaultSelectors.setProductLinkPattern(null); // Accept any URL
        return defaultSelectors;
    }
    
    /**
     * Class to hold selectors for different e-commerce sites
     */
    private static class CategorySelectors {
        private List<String> productSelectors;
        private String nextPageSelector;
        private String baseUrl;
        private Pattern productLinkPattern;
        
        public List<String> getProductSelectors() { return productSelectors; }
        public void setProductSelectors(List<String> productSelectors) { this.productSelectors = productSelectors; }
        
        public String getNextPageSelector() { return nextPageSelector; }
        public void setNextPageSelector(String nextPageSelector) { this.nextPageSelector = nextPageSelector; }
        
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        
        public Pattern getProductLinkPattern() { return productLinkPattern; }
        public void setProductLinkPattern(Pattern pattern) { this.productLinkPattern = pattern; }
    }
} 