package com.travel.dto;

import java.util.List;

public class RagProcessResponse {
    private String enhancedPrompt;
    private List<String> refinedQuestions;
    private double confidenceScore;
    private List<String> relevantContext;

    // Constructors
    public RagProcessResponse() {
    }

    public RagProcessResponse(String enhancedPrompt, List<String> refinedQuestions, double confidenceScore, List<String> relevantContext) {
        this.enhancedPrompt = enhancedPrompt;
        this.refinedQuestions = refinedQuestions;
        this.confidenceScore = confidenceScore;
        this.relevantContext = relevantContext;
    }

    // Getters and setters
    public String getEnhancedPrompt() {
        return enhancedPrompt;
    }

    public void setEnhancedPrompt(String enhancedPrompt) {
        this.enhancedPrompt = enhancedPrompt;
    }

    public List<String> getRefinedQuestions() {
        return refinedQuestions;
    }

    public void setRefinedQuestions(List<String> refinedQuestions) {
        this.refinedQuestions = refinedQuestions;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<String> getRelevantContext() {
        return relevantContext;
    }

    public void setRelevantContext(List<String> relevantContext) {
        this.relevantContext = relevantContext;
    }
}