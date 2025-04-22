package com.webscraper.ScraperService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webscraper.ScraperService.entity.ScrapedData;

@Repository
public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Long>{
    
}