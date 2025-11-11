package utils;

import models.ProductData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    private static final Logger logger = LogManager.getLogger(CSVWriter.class);
    
    public static void writeToCSV(List<ProductData> products, String filename) {
        try (FileWriter csvWriter = new FileWriter(filename)) {
            csvWriter.append("Title,Description,Price,Condition,Ad Posted Date,Seller Name\n");
            
            for (ProductData product : products) {
                csvWriter.append(escapeCsv(product.title)).append(",");
                csvWriter.append(escapeCsv(product.description)).append(",");
                csvWriter.append(escapeCsv(product.price)).append(",");
                csvWriter.append(escapeCsv(product.condition)).append(",");
                csvWriter.append(escapeCsv(product.postedDate)).append(",");
                csvWriter.append(escapeCsv(product.sellerName)).append("\n");
            }
            
            csvWriter.flush();
            logger.info("CSV file created successfully: {}", filename);
        } catch (IOException e) {
            logger.error("Failed to write CSV file", e);
        }
    }
    
    private static String escapeCsv(String value) {
        if (value == null) return "";
        value = value.replace("\"", "\"\"");
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value + "\"";
        }
        return value;
    }
    
    public static void displayTable(List<ProductData> products) {
        System.out.println("\n" + "=".repeat(150));
        System.out.printf("%-40s %-50s %-15s %-12s %-15s %-20s\n", 
            "Title", "Description", "Price", "Condition", "Posted Date", "Seller");
        System.out.println("=".repeat(150));
        
        for (ProductData product : products) {
            System.out.printf("%-40s %-50s %-15s %-12s %-15s %-20s\n",
                truncate(product.title, 40),
                truncate(product.description, 50),
                truncate(product.price, 15),
                truncate(product.condition, 12),
                truncate(product.postedDate, 15),
                truncate(product.sellerName, 20));
        }
        System.out.println("=".repeat(150));
    }
    
    private static String truncate(String value, int length) {
        if (value == null) return "";
        return value.length() > length ? value.substring(0, length - 3) + "..." : value;
    }
}
