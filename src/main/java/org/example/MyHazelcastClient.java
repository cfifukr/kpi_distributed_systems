package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;


public class MyHazelcastClient {
    public static void main(String[] args) throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.getNetworkConfig().addAddress("192.168.0.103:5701", "192.168.0.103:5702", "192.168.0.103:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        IMap<Integer, String> distributedMap = client.getMap("myDistributedMap2");

        for (int i = 0; i <= 1000; i++) {
            distributedMap.put(i, "Value " + i);
            System.out.println(i);
            Thread.sleep(100);
        }

        System.out.println("inserted 1000 entries in Map");

        System.out.println("client is connected to the existing cluster");
    }
}
