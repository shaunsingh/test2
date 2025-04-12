package com.erroranalyzer.controller;

import com.erroranalyzer.model.ErrorRecord;
import com.erroranalyzer.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:3000")
public class ErrorAnalysisController {

    private final AIService aiService;

    @Autowired
    public ErrorAnalysisController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze")
    public String analyzeError(@RequestBody ErrorRecord error) {
        return aiService.analyzeError(error);
    }

    static class ErrorRequest {
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
} 