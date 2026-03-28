package com.travel.controller;

import com.travel.dto.*;
import com.travel.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/import")
    public ApiResponse<TripImportResponse> importTrip(@RequestBody TripImportRequest request) {
        try {
            TripImportResponse response = tripService.importTrip(
                request.getTravelPlan(),
                request.getUserId(),
                request.isMergeWithExisting(),
                request.getTargetTripId()
            );

            if (response.isSuccess()) {
                return ApiResponse.success(response.getMessage(), response);
            } else {
                return ApiResponse.error(response.getMessage());
            }
        } catch (Exception e) {
            return ApiResponse.error("导入行程失败: " + e.getMessage());
        }
    }

    @PostMapping("/prepare")
    public ApiResponse<TravelPlan> prepareTrip(@RequestBody TripPrepareRequest request) {
        try {
            TravelPlan preparedTrip = tripService.prepareTrip(request);
            return ApiResponse.success(preparedTrip);
        } catch (Exception e) {
            return ApiResponse.error("准备行程数据失败: " + e.getMessage());
        }
    }
}