package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import utils.DatabaseUtil;
import utils.ExtentManager;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;
    
    @BeforeSuite
    public void setupSuite() {
        logger.info("Setting up test suite");
        extent = ExtentManager.createInstance();
        DatabaseUtil.initializeDatabase();
        DatabaseUtil.insertOrUpdateWebsite("hamrobazaar", "https://hamrobazaar.com/");
    }
    
    @BeforeMethod
    public void setup() {
        logger.info("Setting up browser");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed");
        }
        ExtentManager.removeTest();
    }
    
    @AfterSuite
    public void tearDownSuite() {
        if (extent != null) {
            extent.flush();
        }
        DatabaseUtil.closeConnection();
        logger.info("Test suite completed");
    }
}