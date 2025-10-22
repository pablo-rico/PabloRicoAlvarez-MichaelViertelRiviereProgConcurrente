package org.starkIndustries.securitySystem.service;

import org.starkIndustries.securitySystem.controller.InMemoryState;
import org.starkIndustries.securitySystem.model.dto.AlertDTO;
import org.starkIndustries.securitySystem.model.enums.SensorType;
import org.starkIndustries.securitySystem.model.enums.Severity;

import java.time.Instant;
import java.util.Random;

public class SimulatorService {

    private final InMemoryState state;


    public SimulatorService(InMemoryState state) {
        this.state = state;
    }

    public void simulate() {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    state.registerReading();
                    if(new Random().nextBoolean()) { //50% possibilities of trigger alert
                        state.registerAlert(new AlertDTO(new Random().nextInt(100000) + "", Severity.random(), "Alert simulated", SensorType.random().toString(), Instant.now().getEpochSecond()));
                    }

                    Thread.sleep(new Random().nextInt(100 * new Random().nextInt(1, 11)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        //t1.run();
    }


}
