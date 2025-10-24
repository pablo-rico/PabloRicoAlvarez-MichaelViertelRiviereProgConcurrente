(function () {
    const totalReadingsEl = document.getElementById("totalReadings");
    const totalAlertsEl = document.getElementById("totalAlerts");
    const epsEl = document.getElementById("eps");
    const alertsTbody = document.querySelector("#alertsTable tbody");

    // Estado en memoria
    let alertsState = [];

    //   Gráfica EPS
    const ctx = document.getElementById("epsChart").getContext("2d");
    const epsData = {
        labels: [],
        datasets: [{ label: "Eventos/seg", data: [] }]
    };
    const epsChart = new Chart(ctx, {
        type: "line",
        data: epsData,
        options: {
            responsive: true,
            animation: false,
            scales: {
                x: { display: false },
                y: { beginAtZero: true }
            }
        }
    });

    //   Utilidades
    function fmt(tsMillis) {
        // tsMillis = epoch ms
        const d = new Date(tsMillis);
        return d.toLocaleTimeString(); // HH:MM:SS según locale
    }

    function renderAlertsTable() {
        // Limpia el <tbody>
        alertsTbody.innerHTML = "";

        // Pintar filas con el estado ya ordenado
        alertsState.forEach(alert => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${fmt(alert.timestamp)}</td>
                <td>${alert.severity}</td>
                <td>${alert.sensorType ?? alert.sensorId ?? "N/A"}</td>
                <td>${alert.message}</td>
            `;
            alertsTbody.appendChild(tr);
        });
    }

    function upsertAlert(alertObj) {
        // Meter nueva alerta en el arreglo y mantener orden DESC
        alertsState.push(alertObj);

        // Ordenar por timestamp descendente (más reciente primero)
        alertsState.sort((a, b) => b.timestamp - a.timestamp);

        // Limitar tamaño para performance en el DOM
        alertsState = alertsState.slice(0, 50);

        // Re-render de la tabla completa
        renderAlertsTable();
    }

    //   Métricas / EPS
    async function refreshMetrics() {
        const res = await fetch("/api/metrics");
        const m = await res.json();

        totalReadingsEl.textContent = m.totalReadings;
        totalAlertsEl.textContent = m.totalAlerts;
        epsEl.textContent = parseFloat(m.eventsPerSecond).toFixed(2);

        // push al chart
        epsData.labels.push(Date.now());
        epsData.datasets[0].data.push(m.eventsPerSecond);

        // Mantener solo 60 puntos (1 min si refrescamos cada segundo)
        if (epsData.labels.length > 60) {
            epsData.labels.shift();
            epsData.datasets[0].data.shift();
        }

        epsChart.update();
    }

    //   Carga inicial alertas
    async function loadRecentAlerts() {
        const res = await fetch("/api/alerts");
        const list = await res.json();

        // 1. Guardamos en memoria
        alertsState = Array.isArray(list) ? list.slice() : [];

        // 2. Ordenamos DESC por timestamp (asumimos que cada alerta tiene .timestamp en ms)
        alertsState.sort((a, b) => b.timestamp - a.timestamp);

        // 3. Limitamos a 50
        alertsState = alertsState.slice(0, 50);

        // 4. Pintamos tabla
        renderAlertsTable();
    }

    //   WebSocket (STOMP)
    function connectWS() {
        const sock = new SockJS("/ws");        // <- debe coincidir con tu WebSocketConfig
        const client = Stomp.over(sock);

        // opcional: bajar el ruido en consola
        client.debug = () => {};
        client.reconnect_delay = 5000;

        client.connect(
            {},
            () => {
                client.subscribe("/topic/alerts", msg => {
                    const a = JSON.parse(msg.body);

                    // Insertar/ordenar/repintar tabla
                    upsertAlert(a);

                    // Incrementar contador visual de alertas sin
                    // esperar al siguiente /metrics
                    const currentTotal = parseInt(totalAlertsEl.textContent || "0", 10);
                    totalAlertsEl.textContent = (currentTotal + 1).toString();
                });
            },
            () => {
                // reconexión básica
                setTimeout(connectWS, 2000);
            }
        );
    }
    //   Arranque
    connectWS();          // escucha alertas nuevas
    loadRecentAlerts();   // llena la tabla inicial
    refreshMetrics();     // primer fetch de métricas
    setInterval(refreshMetrics, 1000); // refresca métricas y EPS cada segundo
})();
