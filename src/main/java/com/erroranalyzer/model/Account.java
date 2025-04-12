package com.erroranalyzer.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Account {
    private Long id;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private LocalDateTime createdAt;
    private List<ErrorRecord> errors;
} 