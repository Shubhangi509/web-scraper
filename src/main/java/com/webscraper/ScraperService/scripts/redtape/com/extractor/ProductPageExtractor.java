package com.webscraper.ScraperService.scripts.redtape.com.extractor;

import com.webscraper.ScraperService.entity.FetchedData;
import com.webscraper.ScraperService.entity.ScrapedData;

import com.webscraper.ScraperService.utils.ProductExtractorScript;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONObject;

@Slf4j
@Component("redtape_com_product")
public class ProductPageExtractor implements ProductExtractorScript {

    @Override
    public ScrapedData extract(FetchedData fetchedData) throws JSONException {
        String url = fetchedData.getUrl();
        log.info("Extracting product data from redtape.com: {}", url);

        String domain = fetchedData.getDomain();
        String pageType = fetchedData.getPageType();

        String html = fetchedData.getHtml();
        // log.info("\n\nHTML content: \n\n\n {} \n\n\n\n\n", html);

        Document doc = Jsoup.parse(html);

        String jsonStr = doc.select("script[type=application/ld+json]").get(1).html();
        JSONObject jsonObject = new JSONObject(jsonStr);

        String title = jsonObject.get("name").toString();
        String productId = doc.select("[name=product-id]").get(0).attr("value");
        // String productId = doc.select(".table-striped").getElementsByTag("td").get(0).text();
        String description = jsonObject.get("description").toString();
        String brand = jsonObject.getJSONObject("brand").get("name").toString();
        String imageUrl = jsonObject.getJSONArray("image").get(0).toString();
        Double mrp = Double.parseDouble(doc.select(".comparePriceGetting").text().replaceAll("\\D", ""))/100;
        Double sellingPrice = Double.parseDouble(jsonObject.getJSONArray("offers").getJSONObject(0).get("price").toString());
        Double discount = mrp - sellingPrice;
        String availability = jsonObject.getJSONArray("offers").getJSONObject(0).get("availability").toString().replaceAll(".*/", "");

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