package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest")
public class RandomNumberController {
	
	@GetMapping("/randomNumber")
	public Mono<Integer> randomNumber() {
	    return Mono.just(6);
	}

}
