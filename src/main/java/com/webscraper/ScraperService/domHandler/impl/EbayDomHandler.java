package com.webscraper.ScraperService.domHandler.impl;

import com.webscraper.ScraperService.domHandler.DomHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ebay_com")
public class EbayDomHandler implements DomHandler {

    @Override
    public String processPageContent(WebDriver webDriver, String url, String pageSource) throws Exception {
        log.info("Ebay dom handler invoked !!");
        return pageSource;
    }
}
