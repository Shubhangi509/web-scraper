package com.webscraper.ScraperService.entity;

public class Data {
    private String apiData;
    private String html;

    public Data(String apiData, String html) {
        this.apiData = apiData;
        this.html = html;
    }

    public String getApiData() {
        return apiData;
    }

    public String getHtml() { 
        return html;
    }
}
