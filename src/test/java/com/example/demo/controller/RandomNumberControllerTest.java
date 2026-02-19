package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.kafka.CustomMessage;
import com.example.demo.kafka.MessageProducer;
import com.example.demo.service.RandomNumberService;

@WebFluxTest(RandomNumberController.class)
class RandomNumberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RandomNumberService randomNumberService;

    @MockitoBean
    private MessageProducer messageProducer;

    @Test
    @WithMockUser
    void testRandomNumber() {
        when(randomNumberService.getRandomNumber()).thenReturn(5);

        webTestClient.get()
                .uri("/rest/randomNumber")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(5);

        verify(messageProducer).sendMessage(any(CustomMessage.class));
    }
}
