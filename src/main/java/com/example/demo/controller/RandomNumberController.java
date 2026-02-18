package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.RandomNumberService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest")
public class RandomNumberController {
	
	@Autowired
	private RandomNumberService randomNumberService;
	@GetMapping("/randomNumber")
	public Mono<Integer> randomNumber() {
	    return Mono.just(randomNumberService.getRandomNumber());
	}

}
