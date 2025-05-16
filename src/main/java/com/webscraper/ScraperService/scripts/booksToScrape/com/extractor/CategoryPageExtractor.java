package com.webscraper.ScraperService.scripts.bookstoscrape.com.extractor;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.utils.CategoryExtractorScript;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import com.webscraper.ScraperService.entity.CategoryData;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("books_toscrape_com_category")
public class CategoryPageExtractor implements CategoryExtractorScript {

    @Override
    public CategoryData extract(FetchedData fetchedData) {
        String url = fetchedData.getUrl();

        log.info("Extracting product urls from: {}", url);

        String html = fetchedData.getHtml();
        String domain = fetchedData.getDomain();
        Document doc = Jsoup.parse(html);

        List<String> productUrls = new ArrayList<>();
        List<Element> productDataList = doc.select(".product_pod");
        for(Element productData : productDataList) {
            String productUrl = "https://" + domain + "/" + productData.select("h3").select("a").attr("href");
            productUrls.add(productUrl);
        }

        log.info("Product urls extracted..");

        String nextPageUrl = "https://" + domain + "/" + doc.select(".next").select("a").attr("href");

        return new CategoryData(productUrls, nextPageUrl);
    }
}
