package org.example;


import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.ArrayList;
import java.util.List;

public class HazelcastCluster {
    public static void main(String[] args) throws InterruptedException{
        List<HazelcastInstance> nodes = new ArrayList<>();


        for (int i = 1; i <= 3; i++) {
            Config config = new Config();
            config.setClusterName("dev");

            QueueConfig queueConfig = new QueueConfig();
            queueConfig.setName("boundedQueue");
            queueConfig.setMaxSize(10);
            config.addQueueConfig(queueConfig);

            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
            HazelcastInstance node = Hazelcast.newHazelcastInstance(config);
            nodes.add(node);
            System.out.println("node " + i + " started");
        }

        System.out.println("All nodes started");




    }
}