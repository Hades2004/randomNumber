package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.demo.kafka.CustomMessage;

@Configuration
public class KafkaConsumerConfig {

    @Value("${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, CustomMessage> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<CustomMessage> valueDeserializer =
                new JsonDeserializer<>(CustomMessage.class);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
