package org.starkIndustries.securitySystem.model.dto;

import org.starkIndustries.securitySystem.model.enums.SensorType;

public record SensorReading(
            String sensorId,
            SensorType type,
            double value,
            long timestamp // epoch millis; opcional (si 0, lo setea el backend)
    ) {}