package com.travel.dto;

public class TripImportRequest {
    private TravelPlan travelPlan;
    private String userId;
    private boolean mergeWithExisting;
    private String targetTripId;

    // Constructors
    public TripImportRequest() {
    }

    public TripImportRequest(TravelPlan travelPlan, String userId, boolean mergeWithExisting, String targetTripId) {
        this.travelPlan = travelPlan;
        this.userId = userId;
        this.mergeWithExisting = mergeWithExisting;
        this.targetTripId = targetTripId;
    }

    // Getters and setters
    public TravelPlan getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(TravelPlan travelPlan) {
        this.travelPlan = travelPlan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMergeWithExisting() {
        return mergeWithExisting;
    }

    public void setMergeWithExisting(boolean mergeWithExisting) {
        this.mergeWithExisting = mergeWithExisting;
    }

    public String getTargetTripId() {
        return targetTripId;
    }

    public void setTargetTripId(String targetTripId) {
        this.targetTripId = targetTripId;
    }
}