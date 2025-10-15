package org.starkIndustries.securitySystem.controller;

import org.springframework.web.bind.annotation.*;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.dto.MetricsDTO;
import org.starkIndustries.securitySystem.model.dto.SensorReading;
import org.starkIndustries.securitySystem.service.ProcessingService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final InMemoryState state;
    private final ProcessingService processing;

    public ApiController(InMemoryState state, ProcessingService processing) {
        this.state = state;
        this.processing = processing;
    }

    @PostMapping("/readings")
    public String ingest(@RequestBody SensorReading reading) {
        long ts = reading.timestamp() == 0 ? System.currentTimeMillis() : reading.timestamp();
        SensorReading normalized = new SensorReading(
                reading.sensorId(), reading.type(), reading.value(), ts
        );
        state.registerReading();
        processing.process(normalized); // asíncrono
        return "OK";
    }

    @GetMapping("/metrics")
    public MetricsDTO metrics() {
        return state.snapshotMetrics();
    }

    @GetMapping("/alerts")
    public List<AlertDTO> alerts() {
        return state.recentAlerts();
    }
}