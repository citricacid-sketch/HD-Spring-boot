package com.travel.dto;

public class RagProcessRequest {
    private String prompt;

    // Constructors
    public RagProcessRequest() {
    }

    public RagProcessRequest(String prompt) {
        this.prompt = prompt;
    }

    // Getters and setters
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}