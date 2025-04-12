package com.erroranalyzer.service;

import com.erroranalyzer.model.ErrorRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    private final ChatLanguageModel model;
    private final int maxTokens;

    public AIService(
            @Value("${langchain4j.open-ai.chat-model.api-key}") String apiKey,
            @Value("${langchain4j.open-ai.chat-model.model-name}") String modelName,
            @Value("${langchain4j.open-ai.chat-model.temperature}") double temperature,
            @Value("${langchain4j.open-ai.chat-model.log-requests}") boolean logRequests,
            @Value("${langchain4j.open-ai.chat-model.log-responses}") boolean logResponses,
            @Value("${langchain4j.open-ai.chat-model.max-tokens:500}") int maxTokens) {
        
        logger.debug("Creating AIService with modelName: {}, temperature: {}, maxTokens: {}", 
            modelName, temperature, maxTokens);
        
        this.maxTokens = maxTokens;
        this.model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .build();
    }

    public String analyzeError(String errorMessage) {
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be empty");
        }

        // Truncate error message if it's too long
        if (errorMessage.length() > maxTokens / 3) {
            errorMessage = errorMessage.substring(0, maxTokens / 3) + "...";
            logger.warn("Error message truncated to fit token limit");
        }

        String prompt = String.format("""
            In one paragraph, analyze this error and provide a concise solution: %s
            """, errorMessage);

        try {
            return model.generate(prompt);
        } catch (Exception e) {
            logger.error("Error analyzing error message", e);
            return "Unable to analyze error due to technical issues. Please try again later.";
        }
    }

    public String analyzeError(ErrorRecord error, List<ErrorRecord> similarErrors, String knowledgeBase) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this error:\n").append(error.getError()).append("\n\n");
        
        if (!similarErrors.isEmpty()) {
            prompt.append("Similar errors found:\n");
            similarErrors.forEach(e -> prompt.append("- ").append(e.getError()).append("\n"));
            prompt.append("\n");
        }
        
        if (knowledgeBase != null && !knowledgeBase.isEmpty()) {
            prompt.append("Relevant knowledge base entry:\n").append(knowledgeBase).append("\n\n");
        }
        
        prompt.append("Please provide:\n1. Root cause analysis\n2. Recommended solution\n3. Prevention tips");
        
        try {
            return model.generate(prompt.toString());
        } catch (Exception e) {
            logger.error("Error analyzing error with context", e);
            return "Unable to analyze error due to technical issues. Please try again later.";
        }
    }

    public String generateAccountInsights(List<ErrorRecord> errors) {
        if (errors == null || errors.isEmpty()) {
            return "No errors found for analysis.";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following error patterns:\n\n");
        
        // Group errors by severity
        Map<String, Long> severityCounts = errors.stream()
            .collect(Collectors.groupingBy(ErrorRecord::getSeverity, Collectors.counting()));
        
        prompt.append("Error distribution by severity:\n");
        severityCounts.forEach((severity, count) -> 
            prompt.append("- ").append(severity).append(": ").append(count).append("\n"));
        
        // Add sample errors
        prompt.append("\nSample errors:\n");
        errors.stream().limit(5).forEach(error -> 
            prompt.append("- ").append(error.getError()).append("\n"));
        
        prompt.append("\nPlease provide:\n1. Overall error pattern analysis\n2. Recommendations for system improvement\n3. Risk assessment");
        
        try {
            return model.generate(prompt.toString());
        } catch (Exception e) {
            logger.error("Error generating account insights", e);
            return "Unable to generate insights due to technical issues. Please try again later.";
        }
    }

    public String exportAnalysis(List<ErrorRecord> errors, String format) {
        if (errors == null || errors.isEmpty()) {
            return "No errors available for analysis.";
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a ").append(format).append(" report for the following errors:\n\n");
        
        errors.forEach(error -> {
            prompt.append("Error: ").append(error.getError())
                  .append("\nSeverity: ").append(error.getSeverity())
                  .append("\nStatus: ").append(error.getStatus())
                  .append("\nTimestamp: ").append(error.getTimestamp())
                  .append("\n\n");
        });
        
        try {
            return model.generate(prompt.toString());
        } catch (Exception e) {
            logger.error("Error generating export analysis", e);
            return "Unable to generate export analysis due to technical issues. Please try again later.";
        }
    }

    public void addKnowledgeBase(String knowledge) {
        if (knowledge == null || knowledge.trim().isEmpty()) {
            throw new IllegalArgumentException("Knowledge base content cannot be empty");
        }

        // In a real implementation, this would store the knowledge in a database
        logger.info("Adding new knowledge to knowledge base: {}", knowledge.substring(0, Math.min(100, knowledge.length())));
    }

    public String generateSummary(List<ErrorRecord> records) {
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException("Records list cannot be empty");
        }

        // Create a concise summary of errors
        String summary = records.stream()
            .map(error -> String.format("%s: %s", 
                error.getAccount(), 
                error.getError().substring(0, Math.min(50, error.getError().length()))))
            .collect(Collectors.joining("; "));

        String prompt = String.format("""
            In one paragraph, summarize these errors and suggest key actions: %s
            """, summary);

        try {
            return model.generate(prompt);
        } catch (Exception e) {
            logger.error("Error generating summary", e);
            return "Unable to generate summary due to technical issues. Please try again later.";
        }
    }
} 