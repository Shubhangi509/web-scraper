package com.webscraper.ScraperService.scripts.books.toscrape.com.extractor;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.utils.CategoryExtractorScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.CategoryData;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("books_toscrape_com_category")
public class CategoryPageExtractor implements CategoryExtractorScript {

    @Override
    public CategoryData extract(FetchedData fetchedData) {
        List<String> productUrls = new ArrayList<>();
        String nextPageUrl = null;
        
        return new CategoryData(productUrls, nextPageUrl);
    }
}
