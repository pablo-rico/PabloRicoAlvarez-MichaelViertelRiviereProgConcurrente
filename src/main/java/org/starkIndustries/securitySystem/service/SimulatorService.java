package org.starkIndustries.securitySystem.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starkIndustries.securitySystem.model.dto.SensorReading;
import org.starkIndustries.securitySystem.model.enums.SensorType;

import java.util.Random;

@Service
public class SimulatorService {

    private final ProcessingService processingService;
    private final Random random = new Random();

    public SimulatorService(ProcessingService processingService) {
        this.processingService = processingService;
    }

    // Genera lecturas simuladas varias veces por segundo
    // fixedRate en milisegundos
    @Scheduled(fixedRate = 200)
    public void generateReadings() {
        long now = System.currentTimeMillis();

        // probabilidad de que un sensor reporte en este ciclo
        if (random.nextDouble() < 0.8)
            simulateOne(now, SensorType.TEMPERATURE, 60 + random.nextDouble() * 30);

        if (random.nextDouble() < 0.7)
            simulateOne(now, SensorType.ACCESS, random.nextDouble() < 0.05 ? 1 : 0);

        if (random.nextDouble() < 0.9)
            simulateOne(now, SensorType.MOTION, random.nextDouble());
    }


    private void simulateOne(long ts, SensorType type, double value) {
        SensorReading reading = new SensorReading(
                ts,
                type,
                value,
                type.name() // usamos el nombre del sensor como identificador simple
        );

        processingService.processReading(reading);
    }
}
