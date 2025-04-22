package com.webscraper.ScraperService.service;

import com.webscraper.ScraperService.utils.Constants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SeleniumDriverManager {

    private static ChromeOptions chromeOptions;
    private static WebDriver webDriver;

    @Setter
    private Instant driverStartTime;
    private Integer maxRequestCount;
    private Integer maxDriverUptime;
    private String userAgent;

    @Setter
    private Integer requestCount = 0;
    private ChromeDriverService chromeDriverService;

    public SeleniumDriverManager(@Value("${selenium.driverPath}") String driverPath,
                                 @Value("${selenium.isHeadless}") Boolean isHeadless,
                                 @Value("${selenium.maxRequestCount}") Integer maxRequestCount,
                                 @Value("${selenium.maxDriverUpTime}") Integer maxDriverUptime,
                                 Constants constants) {
        this.driverStartTime = Instant.now();
        this.requestCount = 0;
        this.maxRequestCount = maxRequestCount;
        this.maxDriverUptime = maxDriverUptime;
        this.userAgent = constants.getUserAgent();

        this.chromeDriverService = createChromeDriverService();
        chromeOptions = createChromeOptions(isHeadless);
        try {
            System.setProperty("webdriver.chrome.driver", driverPath);

            // This line is responsible for spawning new chrome window
            // webDriver = new ChromeDriver(chromeDriverService, chromeOptions);
            log.info("Chrome driver started !!");
        } catch (Exception e) {
            log.error("Error occurred while initiating chrome driver: {}", e.getMessage());
        }
    }

    private ChromeOptions createChromeOptions(Boolean isHeadless) {
        ChromeOptions chromeOptions = new ChromeOptions();
        if(isHeadless) chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--user-agent=" + userAgent);
        chromeOptions.addArguments("--disable-blink-feature=AutomationControlled");
        chromeOptions.setExperimentalOption("excludeSwitches", new ArrayList<>(Arrays.asList("enable-automation")));
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        return chromeOptions;
    }

    private static int findFreePort() {
        try(ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Could not find a free port", e);
        }
    }

    private ChromeDriverService createChromeDriverService() {
        int port = findFreePort();
        return new ChromeDriverService.Builder().usingPort(port).build();
    }

    public WebDriver getWebDriver() {
        log.info("Get webdriver request received");
        return webDriver;
    }

    public void quitDriver() {
        log.info("Quit driver request received");
        if(webDriver != null) webDriver.quit();
        if(chromeDriverService != null) chromeDriverService.stop();
    }

    public WebDriver rebootDriver() throws InterruptedException {
        log.info("Initiating driver reboot");
        quitDriver();
        TimeUnit.SECONDS.sleep(3);
        try {
            chromeDriverService = createChromeDriverService();
            chromeDriverService.start();
            webDriver = new ChromeDriver(chromeDriverService, chromeOptions);
        } catch (Exception e) {
            log.error("Error occurred during driver reboot: {}", e.getMessage());
        }
        log.info("Driver reboot successful");
        return webDriver;
    }

    public WebDriver manageDriverLifecycle() throws InterruptedException {
        Integer driverUptime = Math.toIntExact(Duration.between(driverStartTime, Instant.now()).getSeconds());
        requestCount++;
        if(requestCount >= maxRequestCount || driverUptime >= maxDriverUptime) {
            if(requestCount >= maxRequestCount) log.info("Request threshold reached, initiating driver restart request");
            else log.info("Time threshold request, initiating driver restart request");
            webDriver = rebootDriver();
            setDriverStartTime(Instant.now());
            setRequestCount(0);
        }
        return webDriver;
    }

}
