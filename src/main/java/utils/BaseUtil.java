package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class BaseUtil {
    private static final Logger logger = LogManager.getLogger(BaseUtil.class);
    private WebDriver driver;
    
    public BaseUtil(WebDriver driver) {
        this.driver = driver;
    }
    
    public WebElement getVisibleElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        logger.debug("Found {} elements for locator: {}", elements.size(), locator);
        
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed() && element.isEnabled()) {
                    logger.debug("Found visible element for: {}", locator);
                    return element;
                }
            } catch (Exception e) {
                continue;
            }
        }
        
        logger.error("No visible element found for: {}", locator);
        throw new RuntimeException("No visible element found for: " + locator);
    }
    
    public void clickElement(By locator) {
        WebElement element = getVisibleElement(locator);
        scrollToElement(element);
        try {
            element.click();
            logger.info("Clicked element: {}", locator);
        } catch (Exception e) {
            jsClick(element);
            logger.info("Clicked element with JS: {}", locator);
        }
    }
    
    public void typeText(By locator, String text) {
        WebElement element = getVisibleElement(locator);
        scrollToElement(element);
        jsClick(element);
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '{}' into element: {}", text, locator);
    }
    
    public void selectDropdown(By locator, String value) {
        WebElement element = getVisibleElement(locator);
        scrollToElement(element);
        Select select = new Select(element);
        select.selectByValue(value);
        logger.info("Selected value '{}' from dropdown: {}", value, locator);
    }
    
    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
    
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", element);
    }
    
    public void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted", e);
        }
    }
}