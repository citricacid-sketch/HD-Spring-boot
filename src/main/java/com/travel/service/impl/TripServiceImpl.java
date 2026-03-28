package com.travel.service.impl;

import com.travel.dto.TravelPlan;
import com.travel.dto.TripImportResponse;
import com.travel.dto.TripPrepareRequest;
import com.travel.service.TripService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TripServiceImpl implements TripService {

    @Override
    public TripImportResponse importTrip(TravelPlan travelPlan, String userId, boolean mergeWithExisting, String targetTripId) {
        try {
            // Validate travel plan
            if (travelPlan == null) {
                return new TripImportResponse(null, "旅行计划数据不能为空", false);
            }

            // Generate a trip ID if not provided
            if (travelPlan.getId() == null || travelPlan.getId().isEmpty()) {
                travelPlan.setId("trip-" + System.currentTimeMillis());
            }

            // Set additional metadata
            travelPlan.setStatus("ongoing");
            travelPlan.setSource("ai");

            // In a real system, this would save to a database
            // For mock, we just return success
            String importTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new TripImportResponse(
                travelPlan,
                "行程导入成功！导入时间：" + importTime,
                true
            );
        } catch (Exception e) {
            return new TripImportResponse(null, "导入失败：" + e.getMessage(), false);
        }
    }

    @Override
    public TravelPlan prepareTrip(TripPrepareRequest request) {
        try {
            TravelPlan travelPlan = request.getTravelPlan();

            // Validate required fields
            if (travelPlan.getTitle() == null || travelPlan.getTitle().isEmpty()) {
                travelPlan.setTitle("未命名行程");
            }

            if (travelPlan.getStartDate() == null || travelPlan.getStartDate().isEmpty()) {
                travelPlan.setStartDate(LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            if (travelPlan.getEndDate() == null || travelPlan.getEndDate().isEmpty()) {
                // Assume 3 days if not specified
                travelPlan.setEndDate(LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            // Ensure days are properly formatted
            if (travelPlan.getDays() != null) {
                for (int i = 0; i < travelPlan.getDays().size(); i++) {
                    TravelPlan.Day day = travelPlan.getDays().get(i);
                    if (day.getLabel() == null || day.getLabel().isEmpty()) {
                        day.setLabel("第" + (i + 1) + "天");
                    }
                }
            }

            // Ensure budget exists
            if (travelPlan.getBudget() == null) {
                TravelPlan.Budget budget = new TravelPlan.Budget();
                budget.setTotal(3000);
                budget.setCurrency("CNY");

                TravelPlan.Budget.Breakdown breakdown = new TravelPlan.Budget.Breakdown();
                breakdown.setAccommodation(1000);
                breakdown.setTransportation(500);
                breakdown.setFood(800);
                breakdown.setActivities(400);
                breakdown.setShopping(200);
                breakdown.setOther(100);
                budget.setBreakdown(breakdown);

                travelPlan.setBudget(budget);
            }

            // Ensure transportation exists
            if (travelPlan.getTransportation() == null) {
                TravelPlan.Transportation transportation = new TravelPlan.Transportation();
                transportation.setPrimary("公共交通");
                transportation.setSecondary("出租车");
                transportation.setNotes("根据实际情况调整");
                travelPlan.setTransportation(transportation);
            }

            // Generate a description if not present
            if (travelPlan.getDescription() == null || travelPlan.getDescription().isEmpty()) {
                travelPlan.setDescription("AI生成的旅行计划，包含详细日程安排和预算信息。");
            }

            return travelPlan;
        } catch (Exception e) {
            // Return a minimal valid plan in case of error
            TravelPlan fallbackPlan = new TravelPlan();
            fallbackPlan.setId("trip-fallback-" + System.currentTimeMillis());
            fallbackPlan.setTitle("旅行计划");
            fallbackPlan.setStartDate(LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fallbackPlan.setEndDate(LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            fallbackPlan.setStatus("ongoing");
            fallbackPlan.setSource("ai");
            fallbackPlan.setDescription("基础旅行计划");

            return fallbackPlan;
        }
    }
}