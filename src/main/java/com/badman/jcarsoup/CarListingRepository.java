package com.badman.jcarsoup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarListingRepository extends JpaRepository<CarListing, Long> {
    boolean existsByLink(String link); // to avoid duplicates
}
