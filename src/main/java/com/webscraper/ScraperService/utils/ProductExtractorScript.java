package com.webscraper.ScraperService.utils;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;
import org.json.JSONException;
import org.springframework.stereotype.Component;

@Component
public interface ProductExtractorScript {
    ScrapedData extract(FetchedData fetchedData) throws JSONException;
}
