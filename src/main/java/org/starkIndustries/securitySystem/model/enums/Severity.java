package org.starkIndustries.securitySystem.model.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum Severity {
    INFO,
    WARN,
    CRITICAL;


    public static Severity random() {
        Severity[] values = values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}