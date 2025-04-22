package com.webscraper.ScraperService.utils;

import com.webscraper.ScraperService.entity.FetchedData;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.CategoryData;

@Component
public interface CategoryExtractorScript {
    CategoryData extract(FetchedData fetchedData);
}
