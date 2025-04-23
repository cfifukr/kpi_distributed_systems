package org.example.messaging_service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaConfig {
    private KafkaBrokerResolver nodeDiscovery;

    public KafkaConfig(KafkaBrokerResolver nodeDiscovery) {
        this.nodeDiscovery = nodeDiscovery;
    }
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();


        List<String> kafkaBootstrapServers = nodeDiscovery.getKafkaBrokerAddresses();

        String bootstrapServers = kafkaBootstrapServers.toString().replaceAll(" ", "")
                .replaceAll("\\[", "").replaceAll("]", "");


        System.out.println("FETCHED KAFKA LIST FROM EUREKA "  +  bootstrapServers);

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 50000);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 100);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
