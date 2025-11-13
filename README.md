# Hamrobazaar Product Scraper

A Selenium-based test automation framework to scrape product data from Hamrobazaar.com using Page Object Model (POM) design pattern.

## 📋 Features

- Search and filter products by location and radius
- Sort products by price (Low to High)
- Scrape product details (Title, Price, Condition, etc.)
- Export data to CSV
- Generate HTML test reports
- SQLite database integration
- Comprehensive logging

## 🛠️ Tech Stack

- **Java 11+**
- **Selenium WebDriver 4.15.0**
- **TestNG 7.8.0**
- **Maven 3.6+**
- **Log4j2 2.21.1**
- **ExtentReports 5.1.1**
- **SQLite JDBC 3.44.0**

## 📁 Project Structure
```
hamrobazaar-automation/
├── src/
│   ├── main/java/
│   │   ├── pages/           # Page Objects
│   │   ├── utils/           # Utilities
│   │   ├── models/          # Data Models
│   │   └── services/        # Business Logic
│   ├── main/resources/
│   │   └── log4j2.xml       # Logging Config
│   └── test/java/tests/     # Test Cases
├── pom.xml
└── README.md
```

## 🚀 Setup

### Prerequisites
```bash
java -version    # Java 11+
mvn -version     # Maven 3.6+
```

### Installation
```bash
# Clone repository
git clone <repo-url>
cd hamrobazaar-automation

# Install dependencies
mvn clean install
```

## ▶️ Run Tests
```bash
# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=HamrobazaarTest
```

## 📊 Output Files

After running tests, you'll get:

- **CSV**: `Search_Result.csv` - Scraped product data
- **HTML Report**: `test-output/ExtentReport.html` - Test execution report
- **Logs**: `logs/automation.log` - Detailed execution logs
- **Database**: `hamrobazaar.db` - Website URLs

## 📝 Test Scenario

The test performs these steps:

1. Navigate to Hamrobazaar
2. Search for "Monitor"
3. Filter by location (new road, Kathmandu)
4. Set 5000m radius
5. Apply filters
6. Sort by Low to High price
7. Scrape top 50 products
8. Export to CSV
9. Display results in console

## 🔧 Configuration

### Change Search Parameters

Edit `HamrobazaarTest.java`:
```java
page.searchProduct("Laptop");              // Change product
page.selectLocation("kathmandu", "nepal"); // Change location
page.setRadiusSlider(10000);               // Change radius (meters)
scraper.scrapeProducts(100);               // Change product count
```

### Database

SQLite database stores website URLs:
```sql
INSERT INTO websites(name, url) VALUES('hamrobazaar', 'https://hamrobazaar.com/');
```

## 📖 Key Classes

### Pages
- **BasePage** - Base class with common utilities
- **HamrobazaarPage** - Page-specific actions using @FindBy
- **NavigationHelper** - Handles navigation and URL retrieval

### Utils
- **BaseUtil** - Selenium helper methods (handles mobile+desktop duplicates)
- **CSVWriter** - Export data to CSV
- **DatabaseUtil** - SQLite operations
- **ExtentManager** - Test report management

### Services
- **ProductScraper** - Scrolls and scrapes products


## 📄 CSV Output Format
```csv
Title,Description,Price,Condition,Ad Posted Date,Seller Name
"Dell Monitor","27 inch","27,000","Brand New","2 days ago","Tech Store"
```

## 📈 Reports

Open `test-output/ExtentReport.html` in browser to view:
- Test execution summary
- Step-by-step logs
- Pass/Fail status
- Screenshots (on failure)
