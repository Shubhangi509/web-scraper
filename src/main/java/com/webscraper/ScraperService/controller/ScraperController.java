package com.webscraper.ScraperService.controller;

import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.utils.ClassifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.service.FetchService;
import com.webscraper.ScraperService.service.ExtractService;
import com.webscraper.ScraperService.service.CategoryPageExtractorService;
import com.webscraper.ScraperService.utils.CsvExportUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/scraper")
@CrossOrigin(origins = "*") // Allow cross-origin requests for the frontend
public class ScraperController {
    
    private FetchService fetchService;
    private ExtractService extractService;
    private CategoryPageExtractorService categoryExtractorService;
    private CsvExportUtil csvExportUtil;
    private ClassifierUtil classifierUtil;

    @Autowired
    public ScraperController(FetchService fetchService, ExtractService extractService,
                            CategoryPageExtractorService categoryExtractorService, CsvExportUtil csvExportUtil,
                             ClassifierUtil classifierUtil) {
        log.info("Constructor called for Scraper Controller");
        this.fetchService = fetchService;
        this.extractService = extractService;
        this.categoryExtractorService = categoryExtractorService;
        this.csvExportUtil = csvExportUtil;
        this.classifierUtil = classifierUtil;
    }

    /**
     * Unified scrape endpoint that works for any page type
     * 
     * Process flow:
     * 1. Fetch the data for the provided URL
     * 2. Use classifier to determine page type
     * 3a. If product page: Extract product data
     * 3b. If category page: Extract all product URLs and process each product
     * 4. Return CSV file with all scraped data
     * 
     * @param url The URL to scrape
     * @return CSV data as a downloadable file
     */
    @GetMapping("/scrape")
    public ResponseEntity<byte[]> scrape(@RequestParam(name = "url") String url) {
        log.info("Request to scrape data for url: {}", url);
        
        try {
            // Step 1: Fetch the page data for the URL
            FetchedData fetchedData = fetchService.fetchPage(url);
            log.info("Fetch successful for URL: {}", url);

            String domain = fetchedData.getDomain();

            // Step 2: Classify the page type using the fetched datd
            String pageType = classifierUtil.classifyPageType(fetchedData);
            fetchedData.setPageType(pageType);

            log.info("Data classified as '{}' page type for domain: {}", 
                    pageType, domain);
            
            List<ScrapedData> scrapedDataList = extractService.extractData(fetchedData);
            
            // Step 3: Handle the page according to its type
//            if ("category".equals(pageType)) {
//                log.info("Processing as category page - will extract and process all products");
//
//                // For category pages: Extract all product URLs and process each product
//                // This will handle pagination automatically until no more pages are found
//                dataList = categoryExtractorService.processAllProductsFromCategory(url, fetchedData.getDomain());
//                log.info("Processed category with {} products", dataList.size());
//            } else {
//                // For product pages (or any other type): Just use the single extracted item
//                log.info("Processing as individual product page");
//                dataList.add(scrapedData);
//            }
            
            // Step 4: Convert all scraped data to CSV and return as download
            byte[] csvContent = csvExportUtil.convertToCsv(scrapedDataList);
            log.info("Generated CSV with {} records", scrapedDataList.size());
            
            // Set up response with CSV file for download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = "scraped_data_" + System.currentTimeMillis() + ".csv";
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
            
        } catch (Exception ex) {
            log.error("Scrape unsuccessful with error: {}", ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
