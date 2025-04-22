package com.webscraper.ScraperService.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="scraped_data")
public class ScrapedData {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="domain")
    private String domain;

    @Column(name="url")
    private String url;

    @Column(name="page_type")
    private String pageType;

    @Column(name="product_id")
    private String productId;

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition="TEXT")
    private String description;

    @Column(name="mrp")
    private Double mrp;

    @Column(name="selling_price")
    private Double sellingPrice;

    @Column(name="discount")
    private Double discount;

    @Column(name="brand")
    private String brand;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="availability")
    private String availability;

    
    public ScrapedData() {
        // Default constructor required by JPA
    }

    public ScrapedData(String domain, String url, String pageType, String productId, String title, String description, Double mrp, Double sellingPrice, Double discount) {
        this.domain = domain;
        this.url = url;
        this.pageType = pageType;
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }

    // Getters
    public Long getId() {
        return id;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getPageType() {
        return pageType;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Double getMrp() {
        return mrp;
    }
    
    public Double getSellingPrice() {
        return sellingPrice;
    }
    
    public Double getDiscount() {
        return discount;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getAvailability() {
        return availability;
    }
    
    
    // Setters
    public void setId(Long id) {
        this.id = id;
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
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }
    
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
