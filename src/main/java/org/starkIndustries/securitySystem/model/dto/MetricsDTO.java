package org.starkIndustries.securitySystem.model.dto;

public class MetricsDTO {

    private long totalReadings; // total de lecturas procesadas
    private long totalAlerts; // total de alertas generadas
    private double eventsPerSecond; // tasa actual de eventos por segundo

    public MetricsDTO() {}

    public MetricsDTO(long totalReadings, long totalAlerts, double eventsPerSecond) {
        this.totalReadings = totalReadings;
        this.totalAlerts = totalAlerts;
        this.eventsPerSecond = eventsPerSecond;
    }

    public long getTotalReadings() {
        return totalReadings;
    }

    public void setTotalReadings(long totalReadings) {
        this.totalReadings = totalReadings;
    }

    public long getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public double getEventsPerSecond() {
        return eventsPerSecond;
    }

    public void setEventsPerSecond(double eventsPerSecond) {
        this.eventsPerSecond = eventsPerSecond;
    }
}
