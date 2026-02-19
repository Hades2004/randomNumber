package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class RandomNumberServiceTest {

    private final RandomNumberService randomNumberService = new RandomNumberService();

    @Test
    void testGetRandomNumber() {
        int result = randomNumberService.getRandomNumber();
        assertThat(result).isEqualTo(5);
    }
}
