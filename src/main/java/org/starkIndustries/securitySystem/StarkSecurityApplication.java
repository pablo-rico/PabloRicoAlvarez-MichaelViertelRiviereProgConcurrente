package org.starkIndustries.securitySystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // habilita el simulador si usas SimulatorService
public class StarkSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarkSecurityApplication.class, args);
    }
}
