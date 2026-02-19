package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class SquareNumberServiceTest {

    private final SquareNumberService squareNumberService = new SquareNumberService();

    @Test
    void testGetSquareNumber() {
        assertThat(squareNumberService.getSquareNumber(0)).isZero();
        assertThat(squareNumberService.getSquareNumber(5)).isEqualTo(25);
        assertThat(squareNumberService.getSquareNumber(-3)).isEqualTo(9);
    }
}
