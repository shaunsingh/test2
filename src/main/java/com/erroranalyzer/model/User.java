package com.erroranalyzer.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private List<Account> accounts;
} 