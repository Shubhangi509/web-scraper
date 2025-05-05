package com.webscraper.ScraperService.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.webscraper.ScraperService.entity.FetchedData;

@Slf4j
@Component
public class FileStorageUtil {

    private static final String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\webscraper\\ScraperService\\fetchedDataFiles\\";

    public void saveCurrentData(FetchedData data) {
        String html = data.getHtml();
        saveData(html, "htmlData.txt");
        String apiData = data.getApiData();
        if(!apiData.isEmpty()) saveData(apiData, "apiData.txt");
    }

    private void saveData(String data, String filename) {
        try {
            String filePath = BASE_PATH + filename;
            log.info("Storing fetched data in file: {}", filename);
            FileWriter file = new FileWriter(filePath);
            file.write(data);
            log.info("Succesfully written fetched data to file: {}", filename);
            file.close();
        }
        catch(IOException e) {
            log.error("Error occured while writing fetched data to file: {}", filename);
        }
    }

    public String saveToFile(String url, String apiData, String html) {
        try{
            String filename = hash(url);
            String filePath = BASE_PATH + filename;
            log.info("Storing fetched data as file: {}", filePath);
            writeToFile(apiData, html, filePath);
            log.info("Fetched data saved successfully");
            return filePath;
        }
        catch (IOException e) {
            log.error("Error saving fetched data: {}", e.getMessage());
            return "";
        }
    }

    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing string", e);
        }
    }

    public void writeToFile(String apiData, String html, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("apiData", apiData);
        jsonNode.put("html", html);
        objectMapper.writeValue(new File(filePath), jsonNode);
    }
}
