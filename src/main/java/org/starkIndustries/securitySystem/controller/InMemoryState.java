package org.starkIndustries.securitySystem.controller;

import org.springframework.stereotype.Component;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.dto.MetricsDTO;

import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryState {
    // Métricas y buffers (sin base de datos)
    final AtomicLong totalReadings = new AtomicLong();
    final AtomicLong totalAlerts = new AtomicLong();
    final Queue<AlertDTO> lastAlerts = new ConcurrentLinkedQueue<>();

    // Ventana para EPS (eventos/segundo): contadores por segundo
    private final ConcurrentLinkedQueue<Long> tsWindow = new ConcurrentLinkedQueue<>();
    private final int windowSeconds = 20;

    public void registerReading() {
        totalReadings.incrementAndGet();
        long sec = Instant.now().getEpochSecond();
        tsWindow.add(sec);
        // Limpieza de ventana
        while (!tsWindow.isEmpty() && sec - tsWindow.peek() > windowSeconds) {
            tsWindow.poll();
        }
    }

    public void registerAlert(AlertDTO a) {
        totalAlerts.incrementAndGet();
        lastAlerts.add(a);
        while (lastAlerts.size() > 50) lastAlerts.poll();
    }

    public MetricsDTO snapshotMetrics() {
        long now = Instant.now().getEpochSecond();
        long count = tsWindow.stream().filter(t -> now - t <= windowSeconds).count();
        double eps = count / (double) windowSeconds;
        return new MetricsDTO(totalReadings.get(), totalAlerts.get(), eps);
    }

    List<AlertDTO> recentAlerts() {
        return List.copyOf(lastAlerts);
    }
}