package tests;

import com.aventstack.extentreports.Status;
import models.ProductData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HamrobazaarPage;
import services.ProductScraper;
import utils.CSVWriter;
import utils.DatabaseUtil;
import utils.ExtentManager;
import java.util.List;

public class HamrobazaarTest extends BaseTest {
    
    @Test(priority = 1, description = "Extract Monitor products from Hamrobazaar")
    public void testScrapeMonitorProducts() {
        test = extent.createTest("Scrape Monitor Products", 
            "Search, filter, sort and extract");
        ExtentManager.setTest(test);
        
        try {
            String url = DatabaseUtil.getWebsiteUrl("hamrobazaar");
            Assert.assertNotNull(url, "Website URL not found in database");
            test.log(Status.INFO, "Retrieved URL from database: " + url);
            
            driver.get(url);
            Thread.sleep(2000);
            test.log(Status.PASS, "Navigated to Hamrobazaar");
            
            HamrobazaarPage page = new HamrobazaarPage(driver);
            
            page.searchProduct("Monitor");
            test.log(Status.PASS, "Searched for 'Monitor'");
            
            page.selectLocation("new road", "kathmandu");
            test.log(Status.PASS, "Selected location: new road team, Kathmandu");
            
            page.setRadiusSlider(5000);
            test.log(Status.PASS, "Set radius to 5000m");
            
            page.clickFilter();
            test.log(Status.PASS, "Applied filters");
            
            page.sortByLowToHigh();
            test.log(Status.PASS, "Sorted by Low to High price");
            
            ProductScraper scraper = new ProductScraper(page);
            List<ProductData> products = scraper.scrapeProducts(50);
            test.log(Status.INFO, "Scraped " + products.size() + " products");
            
            Assert.assertTrue(products.size() > 0, "No products were extracted");
            test.log(Status.PASS, "Products extracted successfully");
            
            CSVWriter.writeToCSV(products, "Search_Result.csv");
            test.log(Status.PASS, "CSV file 'Search_Result.csv' created");
            
            CSVWriter.displayTable(products);
            test.log(Status.PASS, "Product table displayed in console");
            
            test.log(Status.PASS, "Test completed successfully");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            logger.error("Test execution failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}