package com.badman.jcarsoup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/scrape")
public class ScraperController {

    private final CarScraperService scraperService;

    public ScraperController(CarScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping
    public ResponseEntity<String> scrapeFromUrl(@RequestBody ScrapeRequest request) {
        int saved = scraperService.scrapeAndSave(request.getUrl());
        return ResponseEntity.ok("Scraped and saved " + saved + " new listings.");
    }

    public static class ScrapeRequest {
        private String url;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
