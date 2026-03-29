package com.travel.service;

import com.travel.dto.TravelPlan;
import com.travel.dto.TripImportResponse;
import com.travel.dto.TripPrepareRequest;
import com.travel.entity.Trip;
import com.travel.entity.TripDay;
import com.travel.entity.TripSlot;

import java.util.List;

public interface TripService {
    // Import trip from AI
    TripImportResponse importTrip(TravelPlan travelPlan, String userId, boolean mergeWithExisting, String targetTripId);
    
    // Prepare trip data
    TravelPlan prepareTrip(TripPrepareRequest request);
    
    // CRUD operations
    Trip createTrip(Trip trip);
    Trip updateTrip(Long id, Trip trip, String userId);
    void deleteTrip(Long id, String userId);
    Trip getTrip(Long id, String userId);
    List<Trip> getUserTrips(String userId);
    List<Trip> getUserTripsByStatus(String userId, String status);
    
    // Day operations
    TripDay createDay(TripDay day);
    TripDay updateDay(Long id, TripDay day);
    void deleteDay(Long id);
    TripDay getDay(Long id);
    List<TripDay> getTripDays(Long tripId);
    
    // Slot operations
    TripSlot createSlot(TripSlot slot);
    TripSlot updateSlot(Long id, TripSlot slot);
    void deleteSlot(Long id);
    TripSlot getSlot(Long id);
    List<TripSlot> getDaySlots(Long dayId);
    
    // Budget and transportation operations
    void updateBudget(Long tripId, Double total, String currency, 
                   Double accommodation, Double transportation, Double food,
                   Double activities, Double shopping, Double other);
    void updateTransportation(Long tripId, String primary, String secondary, String notes);
}