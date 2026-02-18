package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class SquareNumberService {

    public int getSquareNumber(int x) {
        return x*x;
    }
    
}
