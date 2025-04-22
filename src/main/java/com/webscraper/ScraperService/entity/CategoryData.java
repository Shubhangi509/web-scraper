package com.webscraper.ScraperService.entity;

import java.util.List;

public class CategoryData {
    private String nextPageUrl;
    private List<String> productUrls;

    public CategoryData(List<String> productUrls, String nextPageUrl) {
        this.productUrls = productUrls;
        this.nextPageUrl = nextPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public List<String> getProductUrls() {
        return productUrls;
    }
}
