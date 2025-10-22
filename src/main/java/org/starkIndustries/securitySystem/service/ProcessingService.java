package org.starkIndustries.securitySystem.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.starkIndustries.securitySystem.controller.InMemoryState;
import org.starkIndustries.securitySystem.model.dto.SensorReading;
import org.starkIndustries.securitySystem.model.enums.Severity;

@Service
public class ProcessingService {
    private final AlertService alerts;
    private final InMemoryState state;
    private final SimulatorService simulator;

    public ProcessingService(AlertService alerts, InMemoryState state) {
        this.alerts = alerts;
        this.state = state;
        this.simulator = new SimulatorService(state);
        this.simulator.simulate();
    }

    // Procesamiento concurrente por tipo
    @Async
    public void process(SensorReading r) {
        switch (r.type()) {
            case MOTION -> handleMotion(r);
            case TEMPERATURE -> handleTemperature(r);
            case ACCESS -> handleAccess(r);
        }
    }

    private void handleMotion(SensorReading r) {
        // value > 0 implica movimiento detectado
        if (r.value() > 0.5) {
            alerts.raise(Severity.CRITICAL, "Intrusión detectada", r.sensorId());
        }
    }

    private void handleTemperature(SensorReading r) {
        // Umbrales demo
        if (r.value() >= 60.0) {
            alerts.raise(Severity.CRITICAL, "Sobrecalentamiento (" + r.value() + "ºC)", r.sensorId());
        } else if (r.value() >= 45.0) {
            alerts.raise(Severity.WARN, "Temperatura elevada (" + r.value() + "ºC)", r.sensorId());
        }
    }

    private void handleAccess(SensorReading r) {
        // value: 1 = acceso autorizado; 0 = denegado (solo demo)
        if (r.value() < 0.5) {
            alerts.raise(Severity.WARN, "Acceso denegado", r.sensorId());
        }
    }

    @EventListener
    public void onReadingAccepted(SensorReading accepted) {
        // Hook si quisieras hacer correlación centralizada
    }
}
