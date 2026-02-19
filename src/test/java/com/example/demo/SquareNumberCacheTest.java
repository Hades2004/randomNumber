package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import com.example.demo.service.SquareNumberService;

@SpringBootTest
class SquareNumberCacheTest {

    @Autowired
    private SquareNumberService squareNumberService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCaching() {
        int input = 5;
        
        // 1. Initialer Aufruf
        int result1 = squareNumberService.getSquareNumber(input);
        assertThat(result1).isEqualTo(25);

        // 2. Prüfen, ob der Wert im Cache "squares" liegt
        var cache = cacheManager.getCache("squares");
        assertThat(cache).isNotNull();
        assertThat(cache.get(input)).isNotNull();
        assertThat(cache.get(input).get()).isEqualTo(25);

        // 3. Zweiter Aufruf (sollte aus dem Cache kommen)
        int result2 = squareNumberService.getSquareNumber(input);
        assertThat(result2).isEqualTo(25);
    }
}
