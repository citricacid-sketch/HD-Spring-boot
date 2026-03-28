package com.travel.dto;

public class TripImportResponse {
    private TravelPlan trip;
    private String message;
    private boolean success;

    // Constructors
    public TripImportResponse() {
    }

    public TripImportResponse(TravelPlan trip, String message, boolean success) {
        this.trip = trip;
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public TravelPlan getTrip() {
        return trip;
    }

    public void setTrip(TravelPlan trip) {
        this.trip = trip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}