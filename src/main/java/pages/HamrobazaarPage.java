package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class HamrobazaarPage extends BasePage {
    
    private By searchBox = By.name("searchValue");
    private By locationInput = By.name("location");
    private By locationSuggestions = By.cssSelector("div.place--suggestions li");
    private By sliderHandle = By.cssSelector("div.rs-slider-handle");
    private By sliderBar = By.cssSelector("div.rs-slider-bar");
    private By filterButton = By.xpath("//button[contains(.,'Filter')]");
    private By sortDropdown = By.name("sortParam");
    private By productCards = By.cssSelector("div.card-product-linear");
    
    public HamrobazaarPage(WebDriver driver) {
        super(driver);
    }
    
    public void searchProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(searchBox).submit();
        wait.until(ExpectedConditions.urlContains("search"));
        util.waitFor(3000);
        logger.info("Search completed");
    }
    
    public void selectLocation(String location, String city) {
        logger.info("Selecting location: {} in {}", location, city);
        util.typeText(locationInput, location);
        util.waitFor(2000);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(locationSuggestions));
        List<WebElement> suggestions = driver.findElements(locationSuggestions);
        
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText();
            if (text.toLowerCase().contains(location.toLowerCase()) && 
                text.toLowerCase().contains(city.toLowerCase())) {
                suggestion.click();
                logger.info("Selected location: {}", text);
                break;
            }
        }
        util.waitFor(3000);
    }
    
    public void setRadiusSlider(int targetMeters) {
        logger.info("Setting radius slider to: {}m", targetMeters);
        WebElement handle = util.getVisibleElement(sliderHandle);
        util.scrollToElement(handle);
        util.waitFor(500);
        
        WebElement bar = handle.findElement(By.xpath("./..")).findElement(sliderBar);
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
        logger.info("Radius slider set successfully");
    }
    
    public void clickFilter() {
        logger.info("Clicking filter button");
        util.clickElement(filterButton);
        util.waitFor(3000);
        logger.info("Filter applied");
    }
    
    public void sortByLowToHigh() {
        logger.info("Sorting by Low to High");
        util.selectDropdown(sortDropdown, "3");
        util.waitFor(5000);
        
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        util.waitFor(2000);
        logger.info("Sorting applied successfully");
    }
    
    public List<WebElement> getProductCards() {
        return driver.findElements(productCards);
    }
    
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        util.waitFor(2000);
    }
}