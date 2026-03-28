package com.travel.service;

import com.travel.dto.TravelPlan;

public interface PlanService {
    TravelPlan generateTravelPlan(String enhancedPrompt);
}