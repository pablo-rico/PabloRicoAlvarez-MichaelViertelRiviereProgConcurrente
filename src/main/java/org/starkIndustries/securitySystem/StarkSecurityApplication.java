package org.starkIndustries.securitySystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableAsync
public class StarkSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarkSecurityApplication.class, args);
    }


    /* ===================== IN-MEMORY ESTADO ===================== */


    /* ===================== WEBSOCKET (STOMP) ===================== */


    /* ===================== SERVICIOS ===================== */


    /* ===================== CONTROLADORES REST ===================== */


}
