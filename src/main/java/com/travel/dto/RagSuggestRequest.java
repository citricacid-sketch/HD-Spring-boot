package com.travel.dto;

public class RagSuggestRequest {
    private String partialPrompt;

    // Constructors
    public RagSuggestRequest() {
    }

    public RagSuggestRequest(String partialPrompt) {
        this.partialPrompt = partialPrompt;
    }

    // Getters and setters
    public String getPartialPrompt() {
        return partialPrompt;
    }

    public void setPartialPrompt(String partialPrompt) {
        this.partialPrompt = partialPrompt;
    }
}