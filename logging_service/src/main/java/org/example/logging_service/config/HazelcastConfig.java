package org.example.logging_service.config;


import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class HazelcastConfig {
    @Autowired
    private HazelcastNodeDiscovery hazelcastNodeDiscovery;

    @Bean
    public TransportClientFactories<?> transportClientFactories() {
        return new Jersey3TransportClientFactories();
    }

    @Primary
    @Bean
    public HazelcastInstance hazelcastInstance(HazelcastEurekaLifecycleListener listener) {
        Config config = new Config();
        config.setClusterName("spring-hazelcast-cluster");

        config.addListenerConfig(new ListenerConfig(listener));

        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();

        join.getMulticastConfig().setEnabled(false);

        List<String> members = hazelcastNodeDiscovery.getHazelcastAddresses();
        members.forEach(join.getTcpIpConfig()::addMember);

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IMap<String, String> messagesMap(HazelcastInstance instance) {
        return instance.getMap("messagesMap");
    }
}
