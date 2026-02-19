package com.example.demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final KafkaTemplate<String, CustomMessage> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, CustomMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CustomMessage message) {
        kafkaTemplate.send("my-topic", message);
    }
}
