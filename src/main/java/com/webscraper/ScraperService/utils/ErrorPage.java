package com.webscraper.ScraperService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

@Slf4j
@Component
public class ErrorPage {
    public static boolean isErrorPage(HttpResponse<String> response) {
        return response.statusCode() != 200;
    }
}
