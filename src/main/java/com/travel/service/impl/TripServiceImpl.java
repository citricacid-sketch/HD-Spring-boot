package com.travel.service.impl;

import com.travel.dto.TravelPlan;
import com.travel.dto.TripImportResponse;
import com.travel.dto.TripPrepareRequest;
import com.travel.entity.Trip;
import com.travel.entity.TripDay;
import com.travel.entity.TripSlot;
import com.travel.repository.TripRepository;
import com.travel.repository.TripDayRepository;
import com.travel.repository.TripSlotRepository;
import com.travel.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {
    
    private final TripRepository tripRepository;
    private final TripDayRepository tripDayRepository;
    private final TripSlotRepository tripSlotRepository;
    
    @Autowired
    public TripServiceImpl(TripRepository tripRepository, 
                        TripDayRepository tripDayRepository,
                        TripSlotRepository tripSlotRepository) {
        this.tripRepository = tripRepository;
        this.tripDayRepository = tripDayRepository;
        this.tripSlotRepository = tripSlotRepository;
    }

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
    
    @Override
    @Transactional
    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }
    
    @Override
    @Transactional
    public Trip updateTrip(Long id, Trip trip, String userId) {
        Optional<Trip> existingTrip = tripRepository.findById(id);
        if (existingTrip.isPresent()) {
            Trip tripToUpdate = existingTrip.get();
            // Verify ownership
            if (!tripToUpdate.getUserId().equals(userId)) {
                throw new RuntimeException("无权修改此行程");
            }
            // Update fields
            if (trip.getTitle() != null) tripToUpdate.setTitle(trip.getTitle());
            if (trip.getStartDate() != null) tripToUpdate.setStartDate(trip.getStartDate());
            if (trip.getEndDate() != null) tripToUpdate.setEndDate(trip.getEndDate());
            if (trip.getStatus() != null) tripToUpdate.setStatus(trip.getStatus());
            if (trip.getDescription() != null) tripToUpdate.setDescription(trip.getDescription());
            if (trip.getNote() != null) tripToUpdate.setNote(trip.getNote());
            
            // Update budget
            if (trip.getBudgetTotal() != null) tripToUpdate.setBudgetTotal(trip.getBudgetTotal());
            if (trip.getBudgetCurrency() != null) tripToUpdate.setBudgetCurrency(trip.getBudgetCurrency());
            if (trip.getBudgetAccommodation() != null) tripToUpdate.setBudgetAccommodation(trip.getBudgetAccommodation());
            if (trip.getBudgetTransportation() != null) tripToUpdate.setBudgetTransportation(trip.getBudgetTransportation());
            if (trip.getBudgetFood() != null) tripToUpdate.setBudgetFood(trip.getBudgetFood());
            if (trip.getBudgetActivities() != null) tripToUpdate.setBudgetActivities(trip.getBudgetActivities());
            if (trip.getBudgetShopping() != null) tripToUpdate.setBudgetShopping(trip.getBudgetShopping());
            if (trip.getBudgetOther() != null) tripToUpdate.setBudgetOther(trip.getBudgetOther());
            
            // Update transportation
            if (trip.getTransportPrimary() != null) tripToUpdate.setTransportPrimary(trip.getTransportPrimary());
            if (trip.getTransportSecondary() != null) tripToUpdate.setTransportSecondary(trip.getTransportSecondary());
            if (trip.getTransportNotes() != null) tripToUpdate.setTransportNotes(trip.getTransportNotes());
            
            return tripRepository.save(tripToUpdate);
        }
        throw new RuntimeException("行程不存在");
    }
    
    @Override
    @Transactional
    public void deleteTrip(Long id, String userId) {
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("行程不存在"));
        if (!trip.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此行程");
        }
        tripRepository.delete(trip);
    }
    
    @Override
    public Trip getTrip(Long id, String userId) {
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("行程不存在"));
        if (!trip.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问此行程");
        }
        return trip;
    }
    
    @Override
    public List<Trip> getUserTrips(String userId) {
        return tripRepository.findByUserId(userId);
    }
    
    @Override
    public List<Trip> getUserTripsByStatus(String userId, String status) {
        return tripRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    @Transactional
    public TripDay createDay(TripDay day) {
        return tripDayRepository.save(day);
    }
    
    @Override
    @Transactional
    public TripDay updateDay(Long id, TripDay day) {
        TripDay existingDay = tripDayRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("日程不存在"));
        if (day.getLabel() != null) existingDay.setLabel(day.getLabel());
        if (day.getDayDate() != null) existingDay.setDayDate(day.getDayDate());
        if (day.getNotes() != null) existingDay.setNotes(day.getNotes());
        return tripDayRepository.save(existingDay);
    }
    
    @Override
    @Transactional
    public void deleteDay(Long id) {
        tripDayRepository.deleteById(id);
    }
    
    @Override
    public TripDay getDay(Long id) {
        return tripDayRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("日程不存在"));
    }
    
    @Override
    public List<TripDay> getTripDays(Long tripId) {
        return tripDayRepository.findByTripId(tripId);
    }
    
    @Override
    @Transactional
    public TripSlot createSlot(TripSlot slot) {
        return tripSlotRepository.save(slot);
    }
    
    @Override
    @Transactional
    public TripSlot updateSlot(Long id, TripSlot slot) {
        TripSlot existingSlot = tripSlotRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("活动不存在"));
        if (slot.getTime() != null) existingSlot.setTime(slot.getTime());
        if (slot.getEndTime() != null) existingSlot.setEndTime(slot.getEndTime());
        if (slot.getText() != null) existingSlot.setText(slot.getText());
        if (slot.getNote() != null) existingSlot.setNote(slot.getNote());
        return tripSlotRepository.save(existingSlot);
    }
    
    @Override
    @Transactional
    public void deleteSlot(Long id) {
        tripSlotRepository.deleteById(id);
    }
    
    @Override
    public TripSlot getSlot(Long id) {
        return tripSlotRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("活动不存在"));
    }
    
    @Override
    public List<TripSlot> getDaySlots(Long dayId) {
        return tripSlotRepository.findByTripDayId(dayId);
    }
    
    @Override
    @Transactional
    public void updateBudget(Long tripId, Double total, String currency,
                           Double accommodation, Double transportation, Double food,
                           Double activities, Double shopping, Double other) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new RuntimeException("行程不存在"));
        trip.setBudgetTotal(total);
        trip.setBudgetCurrency(currency);
        trip.setBudgetAccommodation(accommodation);
        trip.setBudgetTransportation(transportation);
        trip.setBudgetFood(food);
        trip.setBudgetActivities(activities);
        trip.setBudgetShopping(shopping);
        trip.setBudgetOther(other);
        tripRepository.save(trip);
    }
    
    @Override
    @Transactional
    public void updateTransportation(Long tripId, String primary, String secondary, String notes) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new RuntimeException("行程不存在"));
        trip.setTransportPrimary(primary);
        Trip.setTransportSecondary(secondary);
        trip.setTransportNotes(notes);
        TripRepository.save(trip);
    }
}