package org.starkIndustries.securitySystem.model.dto;

import org.starkIndustries.securitySystem.model.enums.SensorType;

public class SensorReading {

    private long timestamp; // tiempo en milisegundos unix
    private SensorType sensorType; // tipo de sensor
    private double value; // valor medido
    private String source; // identificador interno del sensor

    public SensorReading() {}

    public SensorReading(long timestamp, SensorType sensorType, double value, String source) {
        this.timestamp = timestamp;
        this.sensorType = sensorType;
        this.value = value;
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
