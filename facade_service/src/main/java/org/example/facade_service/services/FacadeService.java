package org.example.facade_service.services;

import org.example.facade_service.data.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.List;
import java.util.UUID;

@Service
public class FacadeService {
    private final RestClient restClient;
    private final MyKafkaProducer kafkaProducer;

    public FacadeService(RestClient restClient,
                         MyKafkaProducer kafkaProducer) {
        this.restClient = restClient;
        this.kafkaProducer = kafkaProducer;
    }

    @Retryable(value = {RestClientException.class, ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 10000))
    public Message handleNewMessage(String message){
        Message messageNew = new Message(UUID.randomUUID().toString(), message);
        //Message messageNew = new Message("1", message);

        kafkaProducer.sendMessage(messageNew);

        restClient.post()
                .uri("/api/logging")
                .header("Idempotency-Key", messageNew.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageNew)
                .retrieve()
                .toEntity(String.class);

        

        return messageNew;
    }

    @Retryable(value = {RestClientException.class, ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<String> getAllMessages(){
        return restClient.get()
                .uri("/api/logging")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

    }

    @Retryable(value = {RestClientException.class, ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String requestToMessagingService(){
        return restClient.get()
                .uri("/api/messaging/")
                .retrieve()
                .body(String.class);
    }

    @Recover
    public void recover(RestClientException e) {
        System.err.println("Сервер не відповідає" + e.getMessage());
    }

}
