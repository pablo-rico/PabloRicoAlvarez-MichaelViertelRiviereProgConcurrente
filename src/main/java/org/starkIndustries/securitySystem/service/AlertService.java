package org.starkIndustries.securitySystem.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.starkIndustries.securitySystem.controller.InMemoryState;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.enums.Severity;

import java.time.Instant;

@Service
public class AlertService {
    private final InMemoryState state;
    private final SimpMessagingTemplate ws;

    public AlertService(InMemoryState state, SimpMessagingTemplate ws) {
        this.state = state;
        this.ws = ws;
    }

    public void raise(Severity sev, String msg, String sensorId) {
        AlertDTO alert = new AlertDTO(
                "A-" + Instant.now().toEpochMilli(),
                sev, msg, sensorId, System.currentTimeMillis());
        state.registerAlert(alert);
        // Empuja a los clientes en tiempo real
        ws.convertAndSend("/topic/alerts", alert);
    }
}