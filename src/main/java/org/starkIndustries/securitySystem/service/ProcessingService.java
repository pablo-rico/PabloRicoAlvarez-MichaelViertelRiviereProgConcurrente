package org.starkIndustries.securitySystem.service;

import org.springframework.stereotype.Service;
import org.starkIndustries.securitySystem.controller.InMemoryState;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.dto.SensorReading;
import org.starkIndustries.securitySystem.model.enums.Severity;
import org.starkIndustries.securitySystem.model.enums.SensorType;

@Service
public class ProcessingService {

    private final InMemoryState state;
    private final AlertService alertService;

    public ProcessingService(InMemoryState state, AlertService alertService) {
        this.state = state;
        this.alertService = alertService;
    }

    // Procesa una lectura de sensor
    // Actualiza contadores de rendimiento
    // Dispara alerta si corresponde
    public void processReading(SensorReading reading) {
        // registramos que hubo una lectura
        state.addReadingTimestamp(reading.getTimestamp());

        // logica de ejemplo
        // aca podrias tener checks por tipo de sensor y valor
        boolean isCritical = false;
        String msg = "Alert simulated";

        if (reading.getSensorType() == SensorType.TEMPERATURE && reading.getValue() > 80) {
            isCritical = true;
            msg = "High temperature";
        } else if (reading.getSensorType() == SensorType.ACCESS && reading.getValue() == 1) {
            isCritical = true;
            msg = "Unauthorized access";
        } else if (reading.getSensorType() == SensorType.MOTION && reading.getValue() > 0.5) {
            isCritical = true;
            msg = "Motion detected";
        }

        if (isCritical) {
            AlertDTO alert = new AlertDTO(
                    System.currentTimeMillis(),
                    Severity.CRITICAL,
                    reading.getSensorType(),
                    msg
            );
            alertService.raiseAlert(alert);
        }
    }
}
