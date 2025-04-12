package com.erroranalyzer.service;

import com.erroranalyzer.model.ErrorRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {
    public void exportToCSV(List<ErrorRecord> records, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.write("User,Account,Error,Timestamp\n");
            
            // Write data
            for (ErrorRecord record : records) {
                writer.write(String.format("%s,%s,%s,%s\n",
                    escapeCsv(record.getUser()),
                    escapeCsv(record.getAccount()),
                    escapeCsv(record.getError()),
                    record.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ));
            }
        }
    }

    public void exportToExcel(List<ErrorRecord> records, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Error Records");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"User", "Account", "Error", "Timestamp"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Create data rows
            int rowNum = 1;
            for (ErrorRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getUser());
                row.createCell(1).setCellValue(record.getAccount());
                row.createCell(2).setCellValue(record.getError());
                row.createCell(3).setCellValue(record.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (var fileOut = new java.io.FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return value;
    }
} 