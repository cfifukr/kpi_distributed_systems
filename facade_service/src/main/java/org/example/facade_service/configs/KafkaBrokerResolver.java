package org.example.facade_service.configs;


import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class KafkaBrokerResolver {

    private final DiscoveryClient discoveryClient;

    public KafkaBrokerResolver(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<String> getKafkaBrokerAddresses() {
        List<String> services = discoveryClient.getServices();
        services.forEach(serviceName -> {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            System.out.println(serviceName + ":" + instances);
        });



        return discoveryClient.getInstances("kafka-node").stream()
                .map(instance -> instance.getHost() + ":" + instance.getPort())
                .collect(Collectors.toList());
    }
}