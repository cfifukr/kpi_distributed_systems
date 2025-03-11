package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoLockHazelcast {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.getNetworkConfig().addAddress("192.168.0.103:5701", "192.168.0.103:5702", "192.168.0.103:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        IMap<String, Integer> map = client.getMap("my-distributed-map");

        map.putIfAbsent("key", 0);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable incrementTask = () -> {
            for (int k = 0; k < 10_000; k++) {
                Integer value = map.get("key");
                value++;
                map.put("key", value);
            }
        };

        for (int i = 0; i < 3; i++) {
            executor.submit(incrementTask);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Final value for key: " + map.get("key"));
    }
}
