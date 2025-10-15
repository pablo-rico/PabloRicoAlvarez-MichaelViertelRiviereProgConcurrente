package org.starkIndustries.securitySystem.model.dto;

public record MetricsDTO(
            long totalReadings,
            long totalAlerts,
            double eventsPerSecond
    ) {}