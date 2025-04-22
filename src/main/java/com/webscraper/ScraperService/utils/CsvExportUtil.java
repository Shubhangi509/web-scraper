package com.webscraper.ScraperService.utils;

import com.opencsv.CSVWriter;
import com.webscraper.ScraperService.entity.ScrapedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class CsvExportUtil {

    /**
     * Convert a list of ScrapedData objects to CSV format
     * @param scrapedDataList List of scraped data
     * @return CSV content as byte array
     */
    public byte[] convertToCsv(List<ScrapedData> scrapedDataList) {
        log.info("Converting {} scraped data items to CSV", scrapedDataList.size());
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            
            // Write header
            String[] header = {
                "Product ID", "Title", "Brand", "Description", "MRP", "Selling Price", 
                "Discount", "Availability", "Image URL", "Product URL", "Domain", "Page Type"
            };
            csvWriter.writeNext(header);
            
            // Write data rows
            for (ScrapedData data : scrapedDataList) {
                String[] row = {
                    data.getProductId(),
                    data.getTitle(),
                    data.getBrand() != null ? data.getBrand() : "",
                    data.getDescription(),
                    data.getMrp() != null ? data.getMrp().toString() : "",
                    data.getSellingPrice() != null ? data.getSellingPrice().toString() : "",
                    data.getDiscount() != null ? data.getDiscount().toString() : "",
                    data.getAvailability() != null ? data.getAvailability() : "",
                    data.getImageUrl() != null ? data.getImageUrl() : "",
                    data.getUrl(),
                    data.getDomain(),
                    data.getPageType()
                };
                csvWriter.writeNext(row);
            }
            
            csvWriter.flush();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating CSV: {}", e.getMessage());
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }
} 