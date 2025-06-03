package com.webscraper.ScraperService.entity;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;

//@Entity
//@Table(name="fetched_data")
public class FetchedData {
    
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name="id")
    private Long id;
    
//    @Column(name="domain")
    private String domain;

//    @Column(name="url")
    private String url;

//    @Column(name="data_path")
    private String fetchedDataPath;

//    @Column(name="page_type")
    private String pageType;

//    @Column(name="html", columnDefinition="TEXT")
    private String html;
    
//    @Column(name="api_data", columnDefinition="TEXT")
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

    public String getDomain() {
        return domain;
    }

    public String getUrl() {
        return url;
    }

    public String getHtml() {
        return html;
    }
    
    public String getApiData() {
        return apiData;
    }
    
    public String getPageType() {
        return pageType;
    }
    
    public void setHtml(String html) {
        this.html = html;
    }
    
    public void setApiData(String apiData) {
        this.apiData = apiData;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
