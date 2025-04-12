package com.erroranalyzer;

import com.erroranalyzer.model.ErrorRecord;
import com.erroranalyzer.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"com.erroranalyzer.service", "com.erroranalyzer.config"})
public class Main {
    private static final String DB_URL = "jdbc:sqlite:error_analyzer.db";
    private static DatabaseService databaseService;
    private static AIService aiService;
    private static ExportService exportService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        
        // Initialize services
        databaseService = new DatabaseService(DB_URL);
        aiService = context.getBean(AIService.class);
        exportService = new ExportService();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nError Analyzer Menu:");
            System.out.println("1. Add error record");
            System.out.println("2. Analyze errors for an account");
            System.out.println("3. Generate summary report");
            System.out.println("4. Export to CSV");
            System.out.println("5. Export to Excel");
            System.out.println("6. Add knowledge base");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    addErrorRecord(scanner);
                    break;
                case 2:
                    analyzeAccountErrors(scanner);
                    break;
                case 3:
                    generateSummary();
                    break;
                case 4:
                    exportToCSV(scanner);
                    break;
                case 5:
                    exportToExcel(scanner);
                    break;
                case 6:
                    addKnowledgeBase(scanner);
                    break;
                case 7:
                    context.close();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addErrorRecord(Scanner scanner) {
        System.out.print("Enter user: ");
        String user = scanner.nextLine();
        System.out.print("Enter account: ");
        String account = scanner.nextLine();
        System.out.print("Enter error: ");
        String error = scanner.nextLine();
        
        ErrorRecord record = new ErrorRecord(user, account, error);
        databaseService.insertErrorRecord(record);
        System.out.println("Error record added successfully.");
    }

    private static void analyzeAccountErrors(Scanner scanner) {
        System.out.print("Enter account to analyze: ");
        String account = scanner.nextLine();
        
        List<ErrorRecord> records = databaseService.getErrorRecords().stream()
                .filter(r -> r.getAccount().equals(account))
                .toList();
        
        if (records.isEmpty()) {
            System.out.println("No errors found for this account.");
            return;
        }
        
        for (ErrorRecord record : records) {
            String analysis = aiService.analyzeError(record.getError());
            System.out.println("\nError: " + record.getError());
            System.out.println("Analysis: " + analysis);
        }
    }

    private static void generateSummary() {
        List<ErrorRecord> records = databaseService.getErrorRecords();
        if (records.isEmpty()) {
            System.out.println("No errors found in the database.");
            return;
        }
        
        StringBuilder allErrors = new StringBuilder();
        for (ErrorRecord record : records) {
            allErrors.append(record.getError()).append("\n");
        }
        
        String summary = aiService.analyzeError(allErrors.toString());
        System.out.println("\nSummary Report:");
        System.out.println(summary);
    }

    private static void exportToCSV(Scanner scanner) {
        System.out.print("Enter output CSV file path: ");
        String filePath = scanner.nextLine();
        
        try {
            exportService.exportToCSV(databaseService.getErrorRecords(), filePath);
            System.out.println("Data exported to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    private static void exportToExcel(Scanner scanner) {
        System.out.print("Enter output Excel file path: ");
        String filePath = scanner.nextLine();
        
        try {
            exportService.exportToExcel(databaseService.getErrorRecords(), filePath);
            System.out.println("Data exported to Excel successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting to Excel: " + e.getMessage());
        }
    }

    private static void addKnowledgeBase(Scanner scanner) {
        System.out.print("Enter knowledge base text: ");
        String knowledge = scanner.nextLine();
        // For now, we'll just store this in the database
        databaseService.insertKnowledgeBase(knowledge);
        System.out.println("Knowledge base updated successfully.");
    }
} 