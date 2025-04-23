package org.example.logging_service.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Component
@EnableScheduling
public class EurekaHazelcastRegistrar {

    private String instanceId;

    public void register() {
        try {
            String eurekaUrl = "http://localhost:8761/eureka/apps/HAZELCAST-LOGGING-NODE";
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = 5701;
            instanceId = "hazelcast-node-" + UUID.randomUUID();

            String json = """
            {
              "instance": {
                "instanceId": "%s",
                "hostName": "%s",
                "app": "HAZELCAST-LOGGING-NODE",
                "ipAddr": "%s",
                "status": "UP",
                "port": {
                  "$": %d,
                  "@enabled": true
                },
                "dataCenterInfo": {
                  "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                  "name": "MyOwn"
                }
              }
            }
            """.formatted(instanceId, ip, ip, port);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(eurekaUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Hazelcast node registered to Eureka: " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deregister() {
        try {
            if (instanceId == null) return;

            String url = "http://localhost:8761/eureka/apps/HAZELCAST-LOGGING-NODE/" + instanceId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Hazelcast node deregistered from Eureka: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 90000)
    public void sendHeartbeat() {
        try {
            if (instanceId == null) return;

            String url = "http://localhost:8761/eureka/apps/HAZELCAST-LOGGING-NODE/" + instanceId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Heartbeat sent to Eureka: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
