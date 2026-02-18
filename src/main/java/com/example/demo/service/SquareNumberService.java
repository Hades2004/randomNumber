package com.example.demo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SquareNumberService {

    @Cacheable("squares")
    public int getSquareNumber(int x) {
        return x*x;
    }
    
}
