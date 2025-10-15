(function () {
    const totalReadingsEl = document.getElementById("totalReadings");
    const totalAlertsEl = document.getElementById("totalAlerts");
    const epsEl = document.getElementById("eps");
    const alertsTbody = document.querySelector("#alertsTable tbody");

    // Gráfica EPS
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
            scales: { x: { display: false }, y: { beginAtZero: true } }
        }
    });

    function fmt(ts) {
        return new Date(ts).toLocaleTimeString();
    }

    async function refreshMetrics() {
        const res = await fetch("/api/metrics");
        const m = await res.json();
        totalReadingsEl.textContent = m.totalReadings;
        totalAlertsEl.textContent = m.totalAlerts;
        epsEl.textContent = m.eventsPerSecond.toFixed(2);

        // push al chart
        epsData.labels.push(Date.now());
        epsData.datasets[0].data.push(m.eventsPerSecond);
        if (epsData.labels.length > 60) { // ~1 min
            epsData.labels.shift();
            epsData.datasets[0].data.shift();
        }
        epsChart.update();
    }

    async function loadRecentAlerts() {
        const res = await fetch("/api/alerts");
        const list = await res.json();
        alertsTbody.innerHTML = "";
        list.slice(-50).forEach(a => addAlertRow(a));
    }

    function addAlertRow(a) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${fmt(a.timestamp)}</td>
      <td>${a.severity}</td>
      <td>${a.sensorId}</td>
      <td>${a.message}</td>
    `;
        alertsTbody.prepend(tr);
        // Limita a 200 filas
        while (alertsTbody.rows.length > 200) alertsTbody.deleteRow(-1);
    }

    // WebSocket (STOMP)
    function connectWS() {
        const sock = new SockJS("/ws");
        const client = Stomp.over(sock);
        client.reconnect_delay = 5000;
        client.debug = () => {}; // silenciar logs

        client.connect({}, () => {
            client.subscribe("/topic/alerts", msg => {
                const a = JSON.parse(msg.body);
                addAlertRow(a);
                // +1 en totalAlerts visual sin esperar al siguiente /metrics
                totalAlertsEl.textContent = (parseInt(totalAlertsEl.textContent || "0", 10) + 1).toString();
            });
        }, () => {
            // reintento simple
            setTimeout(connectWS, 2000);
        });
    }

    // timers
    connectWS();
    loadRecentAlerts();
    refreshMetrics();
    setInterval(refreshMetrics, 1000);
})();
