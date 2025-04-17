package org.example.facade_service.services;

import org.example.facade_service.data.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MyKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MyKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Message message) {
        kafkaTemplate.send("message-topic", message.id(), message.message());
        System.out.println(message  + " - sent to kafka");

    }

}
