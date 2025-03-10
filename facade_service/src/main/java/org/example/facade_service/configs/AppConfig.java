package org.example.facade_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8890")
                .build();
    }

    @Bean
    public RetryTemplate retryTemplate(MyRetryListener retryListener) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(retryListener);
        return retryTemplate;
    }
}
