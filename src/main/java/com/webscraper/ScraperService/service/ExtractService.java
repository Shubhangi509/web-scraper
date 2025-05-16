package com.webscraper.ScraperService.service;

import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.repository.ScrapedDataRepository;
import com.webscraper.ScraperService.utils.ClassifierUtil;
import com.webscraper.ScraperService.utils.FileStorageUtil;
import com.webscraper.ScraperService.utils.ProductExtractorScript;
import lombok.extern.slf4j.Slf4j;
import com.webscraper.ScraperService.entity.CategoryData;
import com.webscraper.ScraperService.utils.CategoryExtractorScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ExtractService {

    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\webscraper\\ScraperService\\fetchedDataFiles\\";

    private final ApplicationContext applicationContext;
    private final ScrapedDataRepository scrapeRepo;
    private final ClassifierUtil classifierUtil;
    private FetchService fetchService;
    private FileStorageUtil fileStorageUtil;


    @Autowired
    public ExtractService(ApplicationContext applicationContext, ScrapedDataRepository scrapeRepo, 
                          ClassifierUtil classifierUtil, FetchService fetchService, FileStorageUtil fileStorageUtil) {
        this.applicationContext = applicationContext;
        this.scrapeRepo = scrapeRepo;
        this.classifierUtil = classifierUtil;
        this.fetchService = fetchService;
        this.fileStorageUtil = fileStorageUtil;
    }

    
    public ScrapedData extractData(FetchedData fetchedData) throws Exception {
        String url = fetchedData.getUrl();
        log.info("Extracting data from fetched content for url: {}", url);
        
        String domain = fetchedData.getDomain();
        String pageType = fetchedData.getPageType();

        String beanName = domain.replace(".", "_") + "_" + pageType;

        log.info("Calling extractor bean with bean name: {}", beanName);

        if(pageType.equals("product")) {
            ScrapedData scrapedData;
            try {
                log.info("Processing as product page - will extract product data");
                ProductExtractorScript extractor = applicationContext.getBean(beanName, ProductExtractorScript.class);
                scrapedData = extractor.extract(fetchedData);
                log.info("Data scraped successfully");
                // scrapeRepo.save(scrapedData);
                return scrapedData;
            } catch (Exception e) {
                log.error("No extractor found for domain {} and page type {}", domain, pageType);
                throw new Exception(e.getMessage());
            }
        }
        else {
            try {
                log.info("Processing as category page - will extract and process all products");
                CategoryExtractorScript extractor = applicationContext.getBean(beanName, CategoryExtractorScript.class);
                CategoryData categoryData = extractor.extract(fetchedData);
                List<String> productUrls = categoryData.getProductUrls();

                // String nextPageUrl = categoryData.getNextPageUrl();
                // if(nextPageUrl != null && !nextPageUrl.isEmpty()) productUrls.add(nextPageUrl);

                String filename = domain.replace(".", "_") + "_Urls.txt";
                fileStorageUtil.saveForNextCall(productUrls, filename);
                // scrapedDataList.addAll(categoryData.getProductUrls());
            } catch (Exception e) {
                log.error("No extractor found for domain {} and page type {}", domain, pageType);
                throw new Exception(e.getMessage());
            }
        }
        return null;
    }
}
