package com.travel.controller;

import com.travel.dto.*;
import com.travel.entity.Trip;
import com.travel.entity.TripDay;
import com.travel.entity.TripSlot;
import com.travel.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    // CRUD operations for trips
    @PostMapping
    public ApiResponse<Trip> createTrip(@RequestBody Trip trip) {
        try {
            Trip createdTrip = tripService.createTrip(trip);
            return ApiResponse.success("行程创建成功", createdTrip);
        } catch (Exception e) {
            return ApiResponse.error("创建行程失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Trip> updateTrip(@PathVariable Long id, 
                                         @RequestBody Trip trip,
                                         @RequestHeader("X-User-Id") String userId) {
        try {
            Trip updatedTrip = tripService.updateTrip(id, trip, userId);
            return ApiResponse.success("行程更新成功", updatedTrip);
        } catch (Exception e) {
            return ApiResponse.error("更新行程失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTrip(@PathVariable Long id,
                                         @RequestHeader("X-User-Id") String userId) {
        try {
            tripService.deleteTrip(id, userId);
            return ApiResponse.success("行程删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除行程失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Trip> getTrip(@PathVariable Long id,
                                   @RequestHeader("X-User-Id") String userId) {
        try {
            Trip trip = tripService.getTrip(id, userId);
            return ApiResponse.success(trip);
        } catch (Exception e) {
            return ApiResponse.error("获取行程失败: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Trip>> getUserTrips(@RequestHeader("X-User-Id") String userId,
                                                @RequestParam(required = false) String status) {
        try {
            List<Trip> trips;
            if (status != null && !status.isEmpty()) {
                trips = tripService.getUserTripsByStatus(userId, status);
            } else {
                trips = tripService.getUserTrips(userId);
            }
            return ApiResponse.success(trips);
        } catch (Exception e) {
            return ApiResponse.error("获取行程列表失败: " + e.getMessage());
        }
    }
    
    // Day operations
    @PostMapping("/{tripId}/days")
    public ApiResponse<TripDay> createDay(@PathVariable Long tripId,
                                       @RequestBody TripDay day) {
        try {
            day.setTrip(tripService.getTrip(tripId, "system")); // TODO: Get real user ID
            TripDay createdDay = tripService.createDay(day);
            return ApiResponse.success("日程创建成功", createdDay);
        } catch (Exception e) {
            return ApiResponse.error("创建日程失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/days/{id}")
    public ApiResponse<TripDay> updateDay(@PathVariable Long id,
                                       @RequestBody TripDay day) {
        try {
            TripDay updatedDay = tripService.updateDay(id, day);
            return ApiResponse.success("日程更新成功", updatedDay);
        } catch (Exception e) {
            return ApiResponse.error("更新日程失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/days/{id}")
    public ApiResponse<Void> deleteDay(@PathVariable Long id) {
        try {
            tripService.deleteDay(id);
            return ApiResponse.success("日程删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除日程失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/{tripId}/days")
    public ApiResponse<List<TripDay>> getTripDays(@PathVariable Long tripId) {
        try {
            List<TripDay> days = tripService.getTripDays(tripId);
            return ApiResponse.success(days);
        } catch (Exception e) {
            return ApiResponse.error("获取日程列表失败: " + e.getMessage());
        }
    }
    
    // Slot operations
    @PostMapping("/days/{dayId}/slots")
    public ApiResponse<TripSlot> createSlot(@PathVariable Long dayId,
                                         @RequestBody TripSlot slot) {
        try {
            slot.setTripDay(tripService.getDay(dayId));
            TripSlot createdSlot = tripService.createSlot(slot);
            return ApiResponse.success("活动创建成功", createdSlot);
        } catch (Exception e) {
            return ApiResponse.error("创建活动失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/slots/{id}")
    public ApiResponse<TripSlot> updateSlot(@PathVariable Long id,
                                         @RequestBody TripSlot slot) {
        try {
            TripSlot updatedSlot = tripService.updateSlot(id, slot);
            return ApiResponse.success("活动更新成功", updatedSlot);
        } catch (Exception e) {
            return ApiResponse.error("更新活动失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/slots/{id}")
    public ApiResponse<Void> deleteSlot(@PathVariable Long id) {
        try {
            tripService.deleteSlot(id);
            return ApiResponse.success("活动删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除活动失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/days/{dayId}/slots")
    public ApiResponse<List<TripSlot>> getDaySlots(@PathVariable Long dayId) {
        try {
            List<TripSlot> slots = tripService.getDaySlots(dayId);
            return ApiResponse.success(slots);
        } catch (Exception e) {
            return ApiResponse.error("获取活动列表失败: " + e.getMessage());
        }
    }
    
    // Budget and transportation operations
    @PutMapping("/{id}/budget")
    public ApiResponse<Void> updateBudget(@PathVariable Long id,
                                       @RequestBody Map<String, Object> budgetData,
                                       @RequestHeader("X-User-Id") String userId) {
        try {
            tripService.updateBudget(
                id,
                ((Number) budgetData.get("total")).doubleValue(),
                (String) budgetData.get("currency"),
                budgetData.containsKey("accommodation") ? ((Number) budgetData.get("accommodation")).doubleValue() : null,
                budgetData.containsKey("transportation") ? ((Number) budgetData.get("transportation")).doubleValue() : null,
                budgetData.containsKey("food") ? ((Number) budgetData.get("food")).doubleValue() : null,
                budgetData.containsKey("activities") ? ((Number) budgetData.get("activities")).doubleValue() : null,
                budgetData.containsKey("shopping") ? ((Number) budgetData.get("shopping")).doubleValue() : null,
                budgetData.containsKey("other") ? ((Number) budgetData.get("other")).doubleValue() : null
            );
            return ApiResponse.success("预算更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error("更新预算失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/transportation")
    public ApiResponse<Void> updateTransportation(@PathVariable Long id,
                                              @RequestBody Map<String, String> transportData,
                                              @RequestHeader("X-User-Id") String userId) {
        try {
            tripService.updateTransportation(
                id,
                transportData.get("primary"),
                transportData.get("secondary"),
                transportData.get("notes")
            );
            return ApiResponse.success("交通方式更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error("更新交通方式失败: " + e.getMessage());
        }
    }
}