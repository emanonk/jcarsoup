package com.badman.jcarsoup;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarPlaywrightScraper {

    public List<CarListing> scrape(String url) {
        List<CarListing> results = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate(url);

            System.out.println("Cookie to be here..");
            page.waitForSelector("#accept-btn");
            page.click("#accept-btn");
            System.out.println("Cookie Passed..");

            page.waitForSelector("xpath=/html/body/div/div/div/div[5]/div/div/div[2]/div/div[3]/div[1]/div/div/div/div[1]/span");


            page.waitForSelector("a.row-anchor");
            Locator cards = page.locator("a.row-anchor");
            int count = cards.count();

            for (int i = 0; i < count; i++) {
                Locator card = cards.nth(i);

                String title = card.locator("h3").textContent().trim().replaceAll("\\s+", " ");

                String miniDescription = card.locator("h3 + p").textContent().trim();//.textContent().trim();
//                Locator manos = card.locator("p.tw-font-medium tw-line-clamp-1");//.textContent().trim();

//                System.out.println(manos.textContent().trim());
//                int cout = card.count();
                String price = card.locator("span").nth(2).textContent();
//                System.out.println(spans);
//                String xpathForDescription = "xpath=//*[@id=\"__layout\"]/div/div[5]/div/div/div[2]/div/div[2]/div/ol/li["+i+"]/div/a/div[2]/div[1]/div/div[1]/p";
//                String xpathForDescription = "xpath=/html/body/div[2]/div/div/div[6]/div/div/div[2]/div/div[2]/div/ol/li["+i+"]/div/a/div[2]/div[1]/div/div[1]/p";
//                page.waitForSelector("a.row-anchor");
//                String description = page.locator(xpathForDescription).textContent().trim();

                int rest = card.locator("div.tw-line-clamp-2 tw-font-medium tw-text-sm tw-text-grey-600").count();

                String detailsSummary = card.locator("div.tw-line-clamp-2.tw-font-medium.tw-text-sm.tw-text-grey-600")
                        .textContent()
                        .replace("\u00a0", " ") // replace non-breaking spaces
                        .replaceAll("\\s+", " ")
                        .trim();

                String[] parts = detailsSummary.split("•");
                String mileage = parts.length > 0 ? parts[0].trim() : "";
                String engineSize = parts.length > 1 ? parts[1].trim() : "";
                String horsepower = parts.length > 2 ? parts[2].trim() : "";
                String fuelType = parts.length > 3 ? parts[3].trim() : "";


                System.out.println(rest);
                String linkSuffix = card.getAttribute("href");
                String link = linkSuffix != null ? "https://www.car.gr" + linkSuffix : "";


//                String description = page.locator("p.tw-font-medium tw-line-clamp-1").textContent().trim();
//                String price = card.locator("span:has-text('€')").textContent().trim().replaceAll("\\s+", " ");
                CarListing car = new CarListing();
                car.setTitle(title);
                car.setDescription(miniDescription);
                car.setPrice(price);
                car.setEngineSize(engineSize);
                car.setFuelType(fuelType);
                car.setHorsePower(horsepower);
                car.setMileage(mileage);
                car.setLink(link);

                results.add(car);
            }

            browser.close();
        }

        return results;
    }
}
