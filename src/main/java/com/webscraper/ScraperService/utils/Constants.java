package com.webscraper.ScraperService.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Constants {
    public List<String> userAgents;

    public Constants() {
        userAgents = new ArrayList<>(List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
        ));
    }

    public String getUserAgent() {
        Random random = new Random();
        String randomUserAgent = userAgents.get(random.nextInt(userAgents.size()));
        return randomUserAgent;
    }
}
