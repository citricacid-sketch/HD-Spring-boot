package com.travel.service;

import com.travel.dto.RagProcessResponse;

import java.util.List;

public interface RagService {
    RagProcessResponse processPrompt(String prompt);
    List<String> getSuggestions(String partialPrompt);
}