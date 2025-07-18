package com.badman.jcarsoup;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarScraperService {

    private final CarPlaywrightScraper playwrightScraper;
    private final CarListingRepository repository;

    public CarScraperService(CarPlaywrightScraper playwrightScraper, CarListingRepository repository) {
        this.playwrightScraper = playwrightScraper;
        this.repository = repository;
    }

    public int scrapeAndSave(String url) {
        List<CarListing> listings = playwrightScraper.scrape(url);
        int saved = 0;

        for (CarListing car : listings) {
            if (!repository.existsByLink(car.getLink())) {
                repository.save(car);
                saved++;
            }
        }

        return saved;
    }
}
