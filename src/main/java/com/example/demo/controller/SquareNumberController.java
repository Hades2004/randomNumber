package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SquareNumberService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SquareNumberController {
	
	private final SquareNumberService squareNumberService;
	
	@GetMapping("/rest/squareNumber")
	public Mono<Integer> squareNumber() {
	    return Mono.just(squareNumberService.getSquareNumber(5));
	}

}
