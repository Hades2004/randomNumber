package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.websocket.HelloMessage;
import com.example.demo.websocket.Greeting;

@Controller
public class WebSocketController {

@MessageMapping("/hello")
@SendTo("/topic/greetings")
public Greeting greeting(HelloMessage message) {
    return new Greeting("Hello " + message.name());
}    
    
}