package org.example.kafka_proxy.logic;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
@EnableScheduling
public class EurekaRegister {
    @Value("${kafka.brokers}")
    private String defaultBrokers;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String EUREKA_BASE_URL = "http://localhost:8761/eureka";
    private static final String APP_NAME = "KAFKA-NODE";

    public final static List<String> registeredBrokers;

    static {
        registeredBrokers = new java.util.concurrent.CopyOnWriteArrayList<>();
    }

    public void registerBroker(String host, int port) {
        String instanceId = host + ":" + port;
        Map<String, Object> request = Map.of(
                "instance", Map.of(
                        "instanceId", instanceId,
                        "hostName", host,
                        "app", APP_NAME,
                        "ipAddr", host,
                        "status", "UP",
                        "port", Map.of("$", port, "@enabled", true),
                        "vipAddress", APP_NAME.toLowerCase(),
                        "secureVipAddress", APP_NAME.toLowerCase(),
                        "dataCenterInfo", Map.of(
                                "@class", "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                                "name", "MyOwn"
                        ),
                        "leaseInfo", Map.of(
                                "renewalIntervalInSecs", 30,
                                "durationInSecs", 90
                        )
                )
        );

        try {
            restTemplate.postForObject(EUREKA_BASE_URL + "/apps/" + APP_NAME, request, String.class);
            registeredBrokers.add(instanceId);
            System.out.println("✅ Registered broker " + instanceId + " to Eureka");
        } catch (Exception e) {
            System.out.println("❌ Failed to register broker " + instanceId + " → " + e.getMessage());
        }
    }

    public void unregisterBroker(String instanceId) {
        try {
            restTemplate.delete(EUREKA_BASE_URL + "/apps/" + APP_NAME + "/" + instanceId);
            registeredBrokers.remove(instanceId);
            System.out.println("Unregistered broker " + instanceId + " from Eureka");
        } catch (Exception e) {
            System.out.println("Could not unregister broker " + instanceId + " → " + e.getMessage());
        }
    }

    public void unregisterAll() {
        for (String instanceId : registeredBrokers) {
            unregisterBroker(instanceId);
        }
        registeredBrokers.clear();
    }

    @Scheduled(fixedRate = 90000)
    public void renewBrokers() {
        for (String instanceId : registeredBrokers) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<>("", headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        EUREKA_BASE_URL + "/apps/" + APP_NAME + "/" + instanceId,
                        HttpMethod.PUT,
                        request,
                        String.class
                );

                System.out.println("🔁 Renewed lease for: " + instanceId + " → " + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("Failed to renew lease for: " + instanceId + " → " + e.getMessage());
            }
        }
    }

    @PostConstruct
    public void onStart() {
        System.out.println("Cleaning up Eureka on proxy start...");
        unregisterAll();

        if (defaultBrokers != null && !defaultBrokers.isBlank()) {
            String[] brokerArray = defaultBrokers.split(",");
            for (String broker : brokerArray) {
                String[] parts = broker.trim().split(":");
                if (parts.length == 2) {
                    String host = parts[0];
                    int port = Integer.parseInt(parts[1]);
                    System.out.println("Registering base broker from config: " + broker);
                    registerBroker(host, port);
                }
            }
        }
    }

    @PreDestroy
    public void onStop() {
        System.out.println("Cleaning up Eureka on proxy shutdown...");
        unregisterAll();
    }
}
