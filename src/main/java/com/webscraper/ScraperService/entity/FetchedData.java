package com.webscraper.ScraperService.entity;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name="fetched_data")
public class FetchedData {

    //    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name="id")
    @Getter
    @Setter
    private Long id;

    //    @Column(name="domain")
    @Getter
    @Setter
    private String domain;

    //    @Column(name="url")
    @Getter
    @Setter
    private String url;

    //    @Column(name="data_path")
    @Getter
    @Setter
    private String fetchedDataPath;

    //    @Column(name="page_type")
    @Getter
    @Setter
    private String pageType;

    //    @Column(name="html", columnDefinition="TEXT")
    @Getter
    @Setter
    private String html;

    //    @Column(name="api_data", columnDefinition="TEXT")
    @Getter
    @Setter
    private String apiData;

    public FetchedData(String domain, String url, String fetchedDataPath, String pageType) {
        this.domain = domain;
        this.url = url;
        this.fetchedDataPath = fetchedDataPath;
        this.pageType = pageType;
    }

    public FetchedData() {
        // Default constructor required by JPA
    }

}