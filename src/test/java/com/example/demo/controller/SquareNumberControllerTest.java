package com.example.demo.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.service.SquareNumberService;

@WebFluxTest(SquareNumberController.class)
class SquareNumberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SquareNumberService squareNumberService;

    @Test
    @WithMockUser
    void testSquareNumber() {
        int input = 4;
        int expected = 16;
        when(squareNumberService.getSquareNumber(input)).thenReturn(expected);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/rest/squareNumber")
                        .queryParam("value", input)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(expected);
    }

    @Test
    @WithMockUser
    void testSquareNumber_MissingParam() {
        webTestClient.get()
                .uri("/rest/squareNumber")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
