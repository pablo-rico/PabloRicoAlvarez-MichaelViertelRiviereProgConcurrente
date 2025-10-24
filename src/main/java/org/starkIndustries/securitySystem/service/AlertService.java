package org.starkIndustries.securitySystem.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.starkIndustries.securitySystem.controller.InMemoryState;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;

@Service
public class AlertService {

    private final InMemoryState state;
    private final SimpMessagingTemplate messagingTemplate;

    public AlertService(InMemoryState state, SimpMessagingTemplate messagingTemplate) {
        this.state = state;
        this.messagingTemplate = messagingTemplate;
    }

    // Agrega la alerta al estado global y la publica por websocket
    public void raiseAlert(AlertDTO alert) {
        state.addAlert(alert);

        // Enviamos la alerta individual al topico de stomp
        // El front escucha en /topic/alerts
        messagingTemplate.convertAndSend("/topic/alerts", alert);
    }
}
