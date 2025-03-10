package org.example.facade_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRetry
public class FacadeServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FacadeServiceApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Microservice Facade Service");
    }
}
