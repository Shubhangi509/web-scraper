package com.webscraper.ScraperService.domHandler;

import org.openqa.selenium.WebDriver;

public interface DomHandler {
    String processPageContent(WebDriver webDriver, String url, String pageSource) throws Exception;
}
