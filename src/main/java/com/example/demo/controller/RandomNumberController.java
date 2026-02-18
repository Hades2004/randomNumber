package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.RandomNumberService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RandomNumberController {
	
	private final RandomNumberService randomNumberService;
	
	@GetMapping("/rest/randomNumber")
	public Mono<Integer> randomNumber() {
	    return Mono.just(randomNumberService.getRandomNumber());
	}

}
