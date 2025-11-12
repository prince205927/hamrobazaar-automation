package services;

import models.ProductData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.HamrobazaarPage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductScraper {
    private static final Logger logger = LogManager.getLogger(ProductScraper.class);
    private HamrobazaarPage page;

    public ProductScraper(HamrobazaarPage page) {
        this.page = page;
    }

    public List<ProductData> scrapeProducts(int targetCount) {
        logger.info("Scraping top {} products", targetCount);
        
        List<ProductData> products = new ArrayList<>();
        Set<String> collectedTitles = new HashSet<>(); 
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted", e);
        }
        
        List<WebElement> productCards = page.getProductCards();
        logger.info("Found {} product cards on page", productCards.size());
        
        for (int i = 0; i < productCards.size() && products.size() < targetCount; i++) {
            try {
                WebElement card = productCards.get(i);
                ProductData data = extractProductData(card);
                
                if (!data.title.equals("N/A") && 
                    !data.title.trim().isEmpty() &&
                    collectedTitles.add(data.title)) { 
                    
                    products.add(data);
                    logger.info("Collected {}/{}: {} - {}", 
                        products.size(), targetCount, data.title, data.price);
                }
            } catch (Exception e) {
                logger.error("Error extracting product at index {}: {}", i, e.getMessage());
            }
        }
        
        if (products.size() < targetCount) {
            logger.warn("Only {} products available (target was {})", 
                products.size(), targetCount);
        }
        
        logger.info("Scraping complete. Total products collected: {}", products.size());
        return products;
    }

  
    private ProductData extractProductData(WebElement product) {
        ProductData data = new ProductData();

        try {
            data.title = product.findElement(By.cssSelector("h2.product-title")).getText();
        } catch (Exception e) {
            logger.debug("Title not found");
        }

        try {
            data.description = product.findElement(By.cssSelector("p.description")).getText();
        } catch (Exception e) {
            logger.debug("Description not found");
        }

        try {
            data.price = product.findElement(By.cssSelector("span.regularPrice")).getText();
        } catch (Exception e) {
            logger.debug("Price not found");
        }

        try {
            data.condition = product.findElement(By.cssSelector("span.condition"))
                .getText().replace("| ", "").trim();
        } catch (Exception e) {
            logger.debug("Condition not found");
        }

        try {
            data.postedDate = product.findElement(By.cssSelector("span.time")).getText();
        } catch (Exception e) {
            logger.debug("Posted date not found");
        }

        try {
            data.sellerName = product.findElement(By.cssSelector("span.username-fullname")).getText();
        } catch (Exception e) {
            logger.debug("Seller name not found");
        }

        return data;
    }
}