package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseUtil;
import java.time.Duration;

public class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected BaseUtil util;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.util = new BaseUtil(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
}