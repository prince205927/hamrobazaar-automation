package tests;

import com.aventstack.extentreports.Status;
import models.ProductData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HamrobazaarPage;
import testutils.NavigationHelper;
import services.ProductScraper;
import utils.CSVWriter;
import utils.ExtentManager;
import java.util.List;

public class HamrobazaarTest extends BaseTest {
    
	@Test(priority = 1, description = "Extract Monitor products from Hamrobazaar")
    public void testScrapeMonitorProducts() {
        test = extent.createTest("Scrape Monitor Products", 
            "Search, filter, sort and extract");
        ExtentManager.setTest(test);
        
        try {
            NavigationHelper navigationHelper = new NavigationHelper(driver);
            navigationHelper.navigateToWebsite("hamrobazaar");
            
            HamrobazaarPage page = new HamrobazaarPage(driver);
            
            page.searchProduct("Monitor");
            
            page.selectLocation("new road", "kathmandu");
            
            page.setRadiusSlider(5000);
            
            page.clickFilter();
            
            page.sortByLowToHigh();
            
            ProductScraper scraper = new ProductScraper(page);
            List<ProductData> products = scraper.scrapeProducts(50);
            
            Assert.assertTrue(products.size() > 0, "No products were scraped");
            
            CSVWriter.writeToCSV(products, "Search_Result.csv");
            
            CSVWriter.displayTable(products);
            
            test.log(Status.PASS, "Test completed successfully with " + products.size() + " products");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            logger.error("Test execution failed", e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}