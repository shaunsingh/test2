package com.erroranalyzer.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRecord {
    private Long id;
    private Integer accountId;
    private String user;
    private String account;
    private String error;
    private String errorText;
    private String status;
    private String severity;
    private LocalDateTime timestamp;
    private String commentary;
} 