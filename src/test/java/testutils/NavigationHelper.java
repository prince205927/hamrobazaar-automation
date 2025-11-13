package testutils;

import com.aventstack.extentreports.Status;

import utils.DatabaseUtil;
import utils.ExtentManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class NavigationHelper {
    private static final Logger logger = LogManager.getLogger(NavigationHelper.class);
    private WebDriver driver;
    private WebDriverWait wait;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateToWebsite(String websiteName) {
        String url = DatabaseUtil.getWebsiteUrl(websiteName);
        
        Assert.assertNotNull(url, "Website URL not found in database for: " + websiteName);
        logger.info("Retrieved URL from database: {}", url);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.INFO, "Retrieved URL from database: " + url);
        }
        
        driver.get(url);
        logger.info("Navigated to: {}", url);
        
        wait.until(ExpectedConditions.urlToBe(url));
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Successfully navigated to " + websiteName);
        }
        logger.info("Page loaded successfully");
    }

    public void waitForPageReady() {
        wait.until(driver -> 
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        logger.debug("Page is ready");
    }
}