package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SquareNumberService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SquareNumberController {
	
	private final SquareNumberService squareNumberService;
	
	@GetMapping("/rest/squareNumber")
	public Mono<Integer> squareNumber(@RequestParam(required = true) int value) {
	    return Mono.just(squareNumberService.getSquareNumber(value));
	}

}
