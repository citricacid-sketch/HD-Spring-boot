package com.travel.controller;

import com.travel.dto.ApiResponse;
import com.travel.dto.PlanGenerateRequest;
import com.travel.dto.TravelPlan;
import com.travel.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/generate")
    public ApiResponse<TravelPlan> generatePlan(@RequestBody PlanGenerateRequest request) {
        try {
            TravelPlan travelPlan = planService.generateTravelPlan(request.getEnhancedPrompt());
            return ApiResponse.success(travelPlan);
        } catch (Exception e) {
            return ApiResponse.error("Failed to generate travel plan: " + e.getMessage());
        }
    }
}