package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "my-topic", groupId = "sample-consumer-group")
    public void listen(CustomMessage message) {
        System.out.println("Received message: " + message.getTitle() + " - " + message.getContent());
    }
}