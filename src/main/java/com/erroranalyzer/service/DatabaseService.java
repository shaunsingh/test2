package com.erroranalyzer.service;

import com.erroranalyzer.model.ErrorRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {
    private final String dbUrl;

    public DatabaseService(@Value("${spring.datasource.url:jdbc:sqlite:error_analyzer.db}") String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Create error_records table with all required fields
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS error_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_id INTEGER NOT NULL,
                    user TEXT NOT NULL,
                    account TEXT NOT NULL,
                    error TEXT NOT NULL,
                    error_text TEXT NOT NULL,
                    status TEXT CHECK(status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'PENDING')) DEFAULT 'OPEN',
                    severity TEXT CHECK(severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')) DEFAULT 'MEDIUM',
                    commentary TEXT,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create knowledge_base table with proper schema
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS knowledge_base (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    error_pattern TEXT NOT NULL,
                    solution TEXT NOT NULL,
                    severity TEXT CHECK(severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')) NOT NULL,
                    category TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public void insertErrorRecord(ErrorRecord record) {
        String sql = """
            INSERT INTO error_records (
                account_id, user, account, error, error_text, status, severity, commentary
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, record.getAccountId());
            pstmt.setString(2, record.getUser());
            pstmt.setString(3, record.getAccount());
            pstmt.setString(4, record.getError());
            pstmt.setString(5, record.getErrorText());
            pstmt.setString(6, record.getStatus() != null ? record.getStatus() : "OPEN");
            pstmt.setString(7, record.getSeverity() != null ? record.getSeverity() : "MEDIUM");
            pstmt.setString(8, record.getCommentary());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error inserting record: " + e.getMessage());
        }
    }

    public List<ErrorRecord> getErrorRecords() {
        List<ErrorRecord> records = new ArrayList<>();
        String sql = """
            SELECT id, account_id, user, account, error, error_text, status, severity, commentary, timestamp 
            FROM error_records 
            ORDER BY timestamp DESC
        """;
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ErrorRecord record = new ErrorRecord();
                record.setId(rs.getLong("id"));
                record.setAccountId(rs.getInt("account_id"));
                record.setUser(rs.getString("user"));
                record.setAccount(rs.getString("account"));
                record.setError(rs.getString("error"));
                record.setErrorText(rs.getString("error_text"));
                record.setStatus(rs.getString("status"));
                record.setSeverity(rs.getString("severity"));
                record.setCommentary(rs.getString("commentary"));
                record.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                records.add(record);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving records: " + e.getMessage());
        }
        
        return records;
    }

    public List<ErrorRecord> getErrorsByAccount(int accountId) {
        List<ErrorRecord> records = new ArrayList<>();
        String sql = """
            SELECT id, account_id, user, account, error, error_text, status, severity, commentary, timestamp 
            FROM error_records 
            WHERE account_id = ? 
            ORDER BY timestamp DESC
        """;
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ErrorRecord record = new ErrorRecord();
                record.setId(rs.getLong("id"));
                record.setAccountId(rs.getInt("account_id"));
                record.setUser(rs.getString("user"));
                record.setAccount(rs.getString("account"));
                record.setError(rs.getString("error"));
                record.setErrorText(rs.getString("error_text"));
                record.setStatus(rs.getString("status"));
                record.setSeverity(rs.getString("severity"));
                record.setCommentary(rs.getString("commentary"));
                record.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                records.add(record);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving records by account: " + e.getMessage());
        }
        
        return records;
    }

    public String getKnowledgeBaseEntry(String errorText) {
        String entry = null;
        String sql = "SELECT solution FROM knowledge_base WHERE error_pattern LIKE ? LIMIT 1";
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + errorText + "%");
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                entry = rs.getString("solution");
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving knowledge base entry: " + e.getMessage());
        }
        
        return entry;
    }

    public void insertKnowledgeBase(String content) {
        String sql = "INSERT INTO knowledge_base (content) VALUES (?)";
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, content);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error inserting knowledge base: " + e.getMessage());
        }
    }
} 