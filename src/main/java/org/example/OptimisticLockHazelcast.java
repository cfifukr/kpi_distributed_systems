package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OptimisticLockHazelcast {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.getNetworkConfig().addAddress("192.168.0.103:5701", "192.168.0.103:5702", "192.168.0.103:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        IMap<String, Integer> map = client.getMap("optimistic-lock-map");

        map.putIfAbsent("key", 0);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        long startTime = System.currentTimeMillis();

        Runnable incrementTask = () -> {
            for (int k = 0; k < 10_000; k++) {
                boolean updated = false;
                while (!updated) {
                    Integer value = map.get("key");
                    Integer newValue = value + 1;
                    updated = map.replace("key", value, newValue);
                }
            }
        };

        for (int i = 0; i < 3; i++) {
            executor.submit(incrementTask);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Final value for key: " + map.get("key"));
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        client.shutdown();
    }
}
