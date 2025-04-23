package org.example.logging_service.config;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HazelcastNodeDiscovery {


    private final DiscoveryClient discoveryClient;

    public HazelcastNodeDiscovery(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<String> getHazelcastAddresses() {
        List<ServiceInstance> instances = discoveryClient.getInstances("HAZELCAST-LOGGING-NODE");

        return instances.stream()
                .map(instance -> instance.getHost() + ":" + instance.getPort())
                .collect(Collectors.toList());
    }
}
