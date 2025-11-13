package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ExtentManager;

import java.util.List;

public class HamrobazaarPage extends BasePage {
    
    @FindBy(name = "searchValue")
    private WebElement searchBox;
    
    @FindBy(name = "location")
    private List<WebElement> locationInputs;
    
    @FindBy(css = "div.place--suggestions li")
    private List<WebElement> locationSuggestions;
    
    @FindBy(css = "div.rs-slider-handle")
    private List<WebElement> sliderHandles;
    
    @FindBy(css = "div.rs-slider-bar")
    private WebElement sliderBar;
    
    @FindBy(xpath = "//button[contains(.,'Filter')]")
    private List<WebElement> filterButtons;
    
    @FindBy(name = "sortParam")
    private List<WebElement> sortDropdowns;
    
    @FindBy(css = "div.card-product-linear")
    private List<WebElement> productCards;
    
    public HamrobazaarPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    public void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        searchBox.sendKeys(productName);
        searchBox.submit();
        wait.until(ExpectedConditions.urlContains("search"));
        util.waitFor(3000);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Searched for '" + productName + "'");
        }
        logger.info("Search completed");
    }
    
    public void selectLocation(String location, String city) {
        logger.info("Selecting location: {} in {}", location, city);
        
        WebElement locationInput = null;
        for (WebElement input : locationInputs) {
            if (input.isDisplayed() && input.isEnabled()) {
                locationInput = input;
                break;
            }
        }
        
        if (locationInput == null) {
            logger.error("No visible location input found");
            return;
        }
        
        util.scrollToElement(locationInput);
        util.waitFor(1000);
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus(); arguments[0].click();", locationInput);
        util.waitFor(500);
        
        locationInput.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        locationInput.sendKeys(org.openqa.selenium.Keys.DELETE);
        util.waitFor(500);
        
        locationInput.sendKeys(location);
        util.waitFor(2000);
        
        wait.until(ExpectedConditions.visibilityOf(locationSuggestions.get(0)));
        
        for (WebElement suggestion : locationSuggestions) {
            String text = suggestion.getText();
            if (text.toLowerCase().contains(location.toLowerCase()) && 
                text.toLowerCase().contains(city.toLowerCase())) {
                suggestion.click();
                logger.info("Selected location: {}", text);
                break;
            }
        }
        
        util.waitFor(3000);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Selected location: " + location + ", " + city);
        }
    }
    
    public void setRadiusSlider(int targetMeters) {
        logger.info("Setting radius slider to: {}m", targetMeters);
        
        WebElement handle = null;
        for (WebElement h : sliderHandles) {
            try {
                if (h.isDisplayed()) {
                    handle = h;
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        
        if (handle == null) {
            logger.error("No visible slider handle found");
            return;
        }
        
        util.scrollToElement(handle);
        util.waitFor(500);
        
        WebElement bar = handle.findElement(org.openqa.selenium.By.xpath("./.."))
                               .findElement(org.openqa.selenium.By.cssSelector("div.rs-slider-bar"));
        
        int sliderWidth = bar.getSize().getWidth();
        double ratio = (targetMeters - 500.0) / (10000.0 - 500.0);
        int offset = (int)(sliderWidth * ratio);
        
        Actions actions = new Actions(driver);
        actions.clickAndHold(handle)
               .moveByOffset(offset, 0)
               .release()
               .build()
               .perform();
        
        util.waitFor(2000);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Set radius to " + targetMeters + "m");
        }
        logger.info("Radius slider set successfully");
    }
    
    public void clickFilter() {
        logger.info("Clicking filter button");
        
        for (WebElement btn : filterButtons) {
            if (btn.isDisplayed() && btn.isEnabled()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                break;
            }
        }
        
        util.waitFor(3000);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Applied filters");
        }
        logger.info("Filter applied");
    }
    
    public void sortByLowToHigh() {
        logger.info("Sorting by Low to High");
        
        WebElement visibleSort = null;
        for (WebElement dropdown : sortDropdowns) {
            if (dropdown.isDisplayed() && dropdown.isEnabled()) {
                visibleSort = dropdown;
                break;
            }
        }
        
        if (visibleSort == null) {
            logger.error("No visible sort dropdown found");
            return;
        }
        
        util.scrollToElement(visibleSort);
        util.waitFor(500);
        
        org.openqa.selenium.support.ui.Select select = 
            new org.openqa.selenium.support.ui.Select(visibleSort);
        select.selectByValue("3");
        
        util.waitFor(7000);
        
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        util.waitFor(2000);
        
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Sorted by Low to High price");
        }
        logger.info("Sorting applied successfully");
    }
    
    public List<WebElement> getProductCards() {
        return productCards;
    }
    
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        util.waitFor(2000);
    }
}