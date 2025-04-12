package com.erroranalyzer.model;

import lombok.Data;

@Data
public class DashboardStats {
    private int totalErrors;
    private int resolvedErrors;
    private int pendingErrors;
    private double averageResolutionTime;
    private SeverityCounts severityCounts;

    @Data
    public static class SeverityCounts {
        private int low;
        private int medium;
        private int high;
        private int critical;

        public SeverityCounts(int low, int medium, int high, int critical) {
            this.low = low;
            this.medium = medium;
            this.high = high;
            this.critical = critical;
        }
    }
} 