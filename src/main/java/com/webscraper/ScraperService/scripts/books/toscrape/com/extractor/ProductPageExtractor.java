package com.webscraper.ScraperService.scripts.books.toscrape.com.extractor;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;
import com.webscraper.ScraperService.utils.Data;
import com.webscraper.ScraperService.utils.ProductExtractorScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


@Slf4j
@Component("books_toscrape_com_product")
public class ProductPageExtractor implements ProductExtractorScript {

    @Override
    public ScrapedData extract(FetchedData fetchedData) {
        String url = fetchedData.getUrl();
        log.info("Extracting product data from books.toscrape.com: {}", url);

        String domain = fetchedData.getDomain();
        String pageType = fetchedData.getPageType();

        String html = fetchedData.getHtml();
        Document doc = Jsoup.parse(html);
        String title = doc.select(".product_main").select("h1").text();
        String productId = doc.select(".table-striped").select("td").get(0).text();
        // String productId = doc.select(".table-striped").getElementsByTag("td").get(0).text();
        String description = doc.select("meta[name=description]").attr("content");
        String brand = "books.toscrape.com";
        String imageUrl = doc.select(".item").select("img").attr("src");
        Double mrp = Double.parseDouble(doc.select(".table-striped").select("td").get(2).text().replace("£", ""));
        Double sellingPrice = Double.parseDouble(doc.select(".table-striped").select("td").get(3).text().replace("£", ""));
        Double discount = mrp - sellingPrice;
        String availability = doc.select(".table-striped").select("td").get(5).text();

        ScrapedData scrapedData = new ScrapedData();
        scrapedData.setDomain(domain);
        scrapedData.setPageType(pageType);
        scrapedData.setUrl(url);
        scrapedData.setProductId(productId);
        scrapedData.setTitle(title);
        scrapedData.setDescription(description);
        scrapedData.setBrand(brand);
        scrapedData.setImageUrl(imageUrl);
        scrapedData.setMrp(mrp);
        scrapedData.setSellingPrice(sellingPrice);
        scrapedData.setDiscount(discount);
        scrapedData.setAvailability(availability);

        return scrapedData;
        
    }
}    
        
        

