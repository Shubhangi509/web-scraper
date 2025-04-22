package com.webscraper.ScraperService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "selenium")
public class SeleniumConfigProperties {

    private String driverPath;
    private Boolean isHeadless;
    private Integer maxRequestCount;
    private Integer maxDriverUpTime;

}
