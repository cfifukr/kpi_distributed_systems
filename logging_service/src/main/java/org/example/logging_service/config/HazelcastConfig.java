package org.example.logging_service.config;


import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class HazelcastConfig {
    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName("spring-hazelcast-cluster");

        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();

        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig()
                .setEnabled(true)
                .addMember("127.0.0.1:5701")
                .addMember("127.0.0.1:5702")
                .addMember("127.0.0.1:5703");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        System.out.println("Hazelcast Cluster Members: " + instance.getCluster().getMembers());

        return instance;
    }


    @Bean
    public IMap<String, String> messagesMap(HazelcastInstance instance) {
        return instance.getMap("messagesMap");
    }

}