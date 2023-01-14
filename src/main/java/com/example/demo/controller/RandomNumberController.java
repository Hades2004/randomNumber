package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/randomNumber")
public class RandomNumberController {
	
	@GetMapping("/")
	private Mono<Integer> getEmployeeById() {
	    return Mono.just(5);
	}

}
