package com.erroranalyzer.service;

import com.erroranalyzer.model.ErrorRecord;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final DatabaseService databaseService;
    private final AIService aiService;

    public AnalysisService(DatabaseService databaseService, AIService aiService) {
        this.databaseService = databaseService;
        this.aiService = aiService;
    }

    public String analyzeError(ErrorRecord error) {
        // Get similar errors from the database
        List<ErrorRecord> similarErrors = databaseService.getErrorsByAccount(error.getAccountId());
        
        // Get knowledge base entry
        String knowledgeBase = databaseService.getKnowledgeBaseEntry(error.getError());

        // Generate AI analysis
        return aiService.analyzeError(error, similarErrors, knowledgeBase);
    }

    public Map<String, Object> getAccountAnalysis(int accountId) {
        List<ErrorRecord> errors = databaseService.getErrorsByAccount(accountId);
        
        // Calculate statistics
        long totalErrors = errors.size();
        long resolvedErrors = errors.stream()
            .filter(e -> "RESOLVED".equals(e.getStatus()))
            .count();
        long pendingErrors = errors.stream()
            .filter(e -> "PENDING".equals(e.getStatus()))
            .count();
        
        // Get most frequent errors
        Map<String, Long> errorFrequency = errors.stream()
            .collect(Collectors.groupingBy(ErrorRecord::getError, Collectors.counting()));
        
        // Generate AI insights
        String insights = aiService.generateAccountInsights(errors);
        
        return Map.of(
            "totalErrors", totalErrors,
            "resolvedErrors", resolvedErrors,
            "pendingErrors", pendingErrors,
            "errorFrequency", errorFrequency,
            "insights", insights
        );
    }

    public String exportAnalysis(int accountId, String format) {
        List<ErrorRecord> errors = databaseService.getErrorsByAccount(accountId);
        return aiService.exportAnalysis(errors, format);
    }
} 