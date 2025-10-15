package org.starkIndustries.securitySystem.model.dto;

import org.starkIndustries.securitySystem.model.enums.Severity;

public record AlertDTO(
        String id,
        Severity severity,
        String message,
        String sensorId,
        long timestamp
) {}