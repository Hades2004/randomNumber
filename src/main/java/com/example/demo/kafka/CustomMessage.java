package com.example.demo.kafka;

public class CustomMessage {
    private String title;
    private String content;

    public CustomMessage() {}

    public CustomMessage(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}