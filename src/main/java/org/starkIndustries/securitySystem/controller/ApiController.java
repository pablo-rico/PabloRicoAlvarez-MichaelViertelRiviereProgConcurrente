package org.starkIndustries.securitySystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.dto.MetricsDTO;

import java.util.List;

@RestController
public class ApiController {

    private final InMemoryState state;

    public ApiController(InMemoryState state) {
        this.state = state;
    }

    // Devuelve las ultimas alertas ya ordenadas de mas nueva a mas vieja
    @GetMapping("/api/alerts")
    public List<AlertDTO> getAlerts() {
        return state.getRecentAlerts();
    }

    // Devuelve las metricas actuales para la cabecera y la grafica
    @GetMapping("/api/metrics")
    public MetricsDTO getMetrics() {
        return state.getMetricsSnapshot();
    }
}
