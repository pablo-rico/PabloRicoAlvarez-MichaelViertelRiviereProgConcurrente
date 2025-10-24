package org.starkIndustries.securitySystem.model.dto;

import org.starkIndustries.securitySystem.model.enums.Severity;
import org.starkIndustries.securitySystem.model.enums.SensorType;

public class AlertDTO {

    private long timestamp; // tiempo en milisegundos unix
    private Severity severity; // nivel de severidad
    private SensorType sensorType; // tipo de sensor que disparo la alerta
    private String message; // descripcion corta de la alerta

    public AlertDTO() {}

    public AlertDTO(long timestamp, Severity severity, SensorType sensorType, String message) {
        this.timestamp = timestamp;
        this.severity = severity;
        this.sensorType = sensorType;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
