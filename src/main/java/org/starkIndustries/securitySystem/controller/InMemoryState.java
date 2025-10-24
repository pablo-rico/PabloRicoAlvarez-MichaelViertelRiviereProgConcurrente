package org.starkIndustries.securitySystem.controller;

import org.springframework.stereotype.Component;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.dto.MetricsDTO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class InMemoryState {

    // Lista segura para concurrencia
    private final CopyOnWriteArrayList<AlertDTO> recentAlerts = new CopyOnWriteArrayList<>();

    // Contadores globales
    private final AtomicLong totalReadings = new AtomicLong(0);
    private final AtomicLong totalAlerts = new AtomicLong(0);

    // Datos para calcular EPS
    // Guardamos una ventana corta de timestamps recientes de lecturas
    private final CopyOnWriteArrayList<Long> recentReadingTimestamps = new CopyOnWriteArrayList<>();

    public void addReadingTimestamp(long ts) {
        totalReadings.incrementAndGet();
        recentReadingTimestamps.add(ts);

        // mantenemos solo el ultimo segundo y un poco mas
        long now = System.currentTimeMillis();
        recentReadingTimestamps.removeIf(t -> now - t > 5000);
    }

    public void addAlert(AlertDTO alert) {
        // sumamos contador global
        totalAlerts.incrementAndGet();

        // agregamos la alerta
        recentAlerts.add(alert);

        // orden descendente por timestamp mas nueva primero
        recentAlerts.sort(Comparator.comparingLong(AlertDTO::getTimestamp).reversed());

        // limitamos el tamano de la lista en memoria
        if (recentAlerts.size() > 200) {
            // nos quedamos con las primeras 200 mas recientes
            List<AlertDTO> top200 = recentAlerts.stream()
                    .limit(200)
                    .collect(Collectors.toList());
            recentAlerts.clear();
            recentAlerts.addAll(top200);
        }
    }

    public List<AlertDTO> getRecentAlerts() {
        // devolvemos copia ordenada desc
        return recentAlerts.stream()
                .sorted(Comparator.comparingLong(AlertDTO::getTimestamp).reversed())
                .limit(50)
                .collect(Collectors.toList());
    }

    public MetricsDTO getMetricsSnapshot() {
        long readingsNow = totalReadings.get();
        long alertsNow = totalAlerts.get();

        // calculo simple de eventos por segundo
        long now = System.currentTimeMillis();
        long cutoff = now - 1000;
        long countLastSecond = recentReadingTimestamps.stream()
                .filter(t -> t >= cutoff)
                .count();

        double eps = (double) countLastSecond;

        return new MetricsDTO(readingsNow, alertsNow, eps);
    }
}
