package com.erroranalyzer.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KnowledgeBase {
    private Long id;
    private String errorPattern;
    private String solution;
    private String severity;
    private String category;
    private LocalDateTime createdAt;
} 