package services;

import models.ProductData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.HamrobazaarPage;
import java.util.ArrayList;
import java.util.List;

public class ProductScraper {
    private static final Logger logger = LogManager.getLogger(ProductScraper.class);
    private HamrobazaarPage page;
    
    public ProductScraper(HamrobazaarPage page) {
        this.page = page;
    }
    
    public List<ProductData> scrapeProducts(int targetCount) {
        logger.info("Starting to scrape {} products", targetCount);
        List<ProductData> products = new ArrayList<>();
        int previousCount = 0;
        int sameCountAttempts = 0;
        
        while (products.size() < targetCount) {
            List<WebElement> productCards = page.getProductCards();
            logger.debug("Found {} product cards on page", productCards.size());
            
            for (int i = products.size(); i < productCards.size() && products.size() < targetCount; i++) {
                try {
                    WebElement product = productCards.get(i);
                    ProductData data = extractProductData(product);
                    
                    if (!data.title.equals("N/A") && !data.title.isEmpty()) {
                        products.add(data);
                        logger.info("Collected: {}/{} - {}", products.size(), targetCount, data.title);
                    }
                } catch (Exception e) {
                    logger.error("Error collecting product", e);
                }
            }
            
            if (products.size() == previousCount) {
                sameCountAttempts++;
                if (sameCountAttempts >= 3) {
                    logger.warn("No new products loaded after 3 attempts");
                    break;
                }
            } else {
                sameCountAttempts = 0;
            }
            previousCount = products.size();
            
            if (products.size() < targetCount) {
                page.scrollToBottom();
            }
        }
        
        logger.info("Total products collected: {}", products.size());
        return products;
    }
    
    private ProductData extractProductData(WebElement product) {
        ProductData data = new ProductData();
        
        try {
            data.title = product.findElement(By.cssSelector("h2.product-title")).getText();
        } catch (Exception e) {}
        
        try {
            data.description = product.findElement(By.cssSelector("p.description")).getText();
        } catch (Exception e) {}
        
        try {
            data.price = product.findElement(By.cssSelector("span.regularPrice")).getText();
        } catch (Exception e) {}
        
        try {
            data.condition = product.findElement(By.cssSelector("span.condition"))
                .getText().replace("| ", "").trim();
        } catch (Exception e) {}
        
        try {
            data.postedDate = product.findElement(By.cssSelector("span.time")).getText();
        } catch (Exception e) {}
        
        try {
            data.sellerName = product.findElement(By.cssSelector("span.username-fullname")).getText();
        } catch (Exception e) {}
        
        return data;
    }
}
