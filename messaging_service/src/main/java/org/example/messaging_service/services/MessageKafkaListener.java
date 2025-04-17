package org.example.messaging_service.services;


import org.example.messaging_service.storage.MessageStorage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageKafkaListener {

    @KafkaListener(topics = "message-topic", groupId = "my-group")
    public void listen(String message) {
        System.out.println("Received: " + message);
        MessageStorage.addMessage(message);
    }
}
