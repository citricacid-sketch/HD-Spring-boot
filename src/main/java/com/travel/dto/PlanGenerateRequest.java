package com.travel.dto;

public class PlanGenerateRequest {
    private String enhancedPrompt;

    // Constructors
    public PlanGenerateRequest() {
    }

    public PlanGenerateRequest(String enhancedPrompt) {
        this.enhancedPrompt = enhancedPrompt;
    }

    // Getters and setters
    public String getEnhancedPrompt() {
        return enhancedPrompt;
    }

    public void setEnhancedPrompt(String enhancedPrompt) {
        this.enhancedPrompt = enhancedPrompt;
    }
}