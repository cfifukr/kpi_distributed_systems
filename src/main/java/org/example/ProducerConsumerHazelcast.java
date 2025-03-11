package org.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerHazelcast {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();


        clientConfig.getNetworkConfig().addAddress("192.168.0.103:5701", "192.168.0.103:5702", "192.168.0.103:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        IQueue<Integer> queue = client.getQueue("boundedQueue");



        queue.clear();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable producerTask = () -> {
            for (int i = 1; i <= 100; i++) {
                try {
                    queue.put(i);
                    System.out.println("Produced: " + i  + Thread.currentThread().getName() + "     Queue size: " + queue.size() );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumerTask = () -> {
            while (true) {
                try {
                    Integer value = queue.take();
                    System.out.println("Consumed: " + value  + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        executor.submit(producerTask);
        executor.submit(producerTask);
        executor.submit(consumerTask);
        //executor.submit(consumerTask);

        executor.shutdown();
    }
}
