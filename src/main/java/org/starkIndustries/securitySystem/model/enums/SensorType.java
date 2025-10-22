package org.starkIndustries.securitySystem.model.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum SensorType {
    MOTION,
    TEMPERATURE,
    ACCESS;


    public static SensorType random() {
        SensorType[] values = values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}