package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.kafka.CustomMessage;
import com.example.demo.kafka.MessageProducer;
import com.example.demo.service.RandomNumberService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RandomNumberController {
	
	private final RandomNumberService randomNumberService;
	private final MessageProducer messageProducer;
	
	@GetMapping("/rest/randomNumber")
	public Mono<Integer> randomNumber() {
		int randomNumber = randomNumberService.getRandomNumber();
		messageProducer.sendMessage(new CustomMessage("RandomNumber", "Produced random number: " + randomNumber));
	    return Mono.just(randomNumber);
	}

}
