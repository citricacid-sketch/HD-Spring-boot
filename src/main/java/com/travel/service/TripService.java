package com.travel.service;

import com.travel.dto.TravelPlan;
import com.travel.dto.TripImportResponse;
import com.travel.dto.TripPrepareRequest;

public interface TripService {
    TripImportResponse importTrip(TravelPlan travelPlan, String userId, boolean mergeWithExisting, String targetTripId);
    TravelPlan prepareTrip(TripPrepareRequest request);
}