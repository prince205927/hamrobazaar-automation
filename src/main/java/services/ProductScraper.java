package services;

import models.ProductData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.HamrobazaarPage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductScraper {
    private static final Logger logger = LogManager.getLogger(ProductScraper.class);
    private HamrobazaarPage page;
    private JavascriptExecutor js;

    public ProductScraper(HamrobazaarPage page) {
        this.page = page;
        try {
            java.lang.reflect.Field field = page.getClass().getSuperclass().getDeclaredField("driver");
            field.setAccessible(true);
            WebDriver driver = (WebDriver) field.get(page);
            this.js = (JavascriptExecutor) driver;
        } catch (Exception e) {
            logger.error("Error accessing driver", e);
        }
    }

    public List<ProductData> scrapeProducts(int targetCount) {
        logger.info("Scraping {} products", targetCount);
        
        List<ProductData> products = new ArrayList<>();
        Set<String> uniqueTitles = new HashSet<>();
        
        while (products.size() < targetCount) {
            for (WebElement card : page.getProductCards()) {
                if (products.size() >= targetCount) break;
                
                ProductData data = extractProductData(card);
                if (isValid(data) && uniqueTitles.add(data.title)) {
                    products.add(data);
                    logger.info("Collected {}/{}: {}", products.size(), targetCount, data.title);
                }
            }
            
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            sleep(3000);
        }
        
        logger.info("Done. Collected: {}", products.size());
        return products;
    }

    private ProductData extractProductData(WebElement product) {
        ProductData data = new ProductData();
        try { 
        	data.title = product.findElement(By.cssSelector("h2.product-title")).getText(); 
        	} catch (Exception e) {
        		
        	}
        try {
        	data.description = product.findElement(By.cssSelector("p.description")).getText(); 
        	} catch (Exception e) {
        		
        	}
        try {
        	 String rawPrice = product.findElement(By.cssSelector("span.regularPrice")).getText();
        	    data.price = rawPrice.replace("रू.", "").trim();        	
        	} catch (Exception e) {
        		
        	}
        try { 
        	data.condition = product.findElement(By.cssSelector("span.condition")).getText().replace("| ", "").trim(); 
        	} catch (Exception e) {
        		
        	}
        try {
        	data.postedDate = product.findElement(By.cssSelector("span.time")).getText();
        	} catch (Exception e) {
        		
        	}
        try { 
        	data.sellerName = product.findElement(By.cssSelector("span.username-fullname")).getText();
        	} catch (Exception e) {
        		
        	}
        return data;
    }
    
    private boolean isValid(ProductData data) {
        return !data.title.equals("N/A") && !data.title.trim().isEmpty();
    }
    
    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}