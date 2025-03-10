package org.example.facade_service.services;

import org.example.facade_service.data.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.log.LogMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

@Service
public class FacadeService {
    private final RestClient restClient;

    public FacadeService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retryable(value = { RestClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Message handleNewMessage(String message){
        Message messageNew = new Message(UUID.randomUUID().toString(), message);

        ResponseEntity<String> response = restClient.post()
                .uri("http://localhost:8890/api/logging")
                .header("Idempotency-Key", messageNew.id())
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageNew)
                .retrieve()
                .toEntity(String.class);

        return messageNew;
    }

    @Retryable(value = { RestClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<String> getAllMessages(){
        return restClient.get()
                .uri("http://localhost:8890/api/logging")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});

    }

    @Retryable(value = { RestClientException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String requestToMessagingService(){
        return restClient.get()
                .uri("http://localhost:8890/api/messaging")
                .retrieve()
                .body(String.class);
    }

    @Recover
    public void recover(RestClientException e) {
        System.err.println("Сервер не відповідає" + e.getMessage());
    }

}
