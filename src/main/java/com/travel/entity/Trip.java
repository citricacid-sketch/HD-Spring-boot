package com.travel.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String status; // "ongoing", "completed", "draft"

    @Column(nullable = false)
    private String source; // "ai", "manual"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String note;

    // Budget fields
    @Column(name = "budget_total")
    private Double budgetTotal;

    @Column(name = "budget_currency")
    private String budgetCurrency;

    @Column(name = "budget_accommodation")
    private Double budgetAccommodation;

    @Column(name = "budget_transportation")
    private Double budgetTransportation;

    @Column(name = "budget_food")
    private Double budgetFood;

    @Column(name = "budget_activities")
    private Double budgetActivities;

    @Column(name = "budget_shopping")
    private Double budgetShopping;

    @Column(name = "budget_other")
    private Double budgetOther;

    // Transportation fields
    @Column(name = "transport_primary")
    private String transportPrimary;

    @Column(name = "transport_secondary")
    private String transportSecondary;

    @Column(name = "transport_notes")
    private String transportNotes;

    // One-to-many relationship with days
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripDay> days = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Budget getters and setters
    public Double getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(Double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public String getBudgetCurrency() {
        return budgetCurrency;
    }

    public void setBudgetCurrency(String budgetCurrency) {
        this.budgetCurrency = budgetCurrency;
    }

    public Double getBudgetAccommodation() {
        return budgetAccommodation;
    }

    public void setBudgetAccommodation(Double budgetAccommodation) {
        this.budgetAccommodation = budgetAccommodation;
    }

    public Double getBudgetTransportation() {
        return budgetTransportation;
    }

    public void setBudgetTransportation(Double budgetTransportation) {
        this.budgetTransportation = budgetTransportation;
    }

    public Double getBudgetFood() {
        return budgetFood;
    }

    public void setBudgetFood(Double budgetFood) {
        this.budgetFood = budgetFood;
    }

    public Double getBudgetActivities() {
        return budgetActivities;
    }

    public void setBudgetActivities(Double budgetActivities) {
        this.budgetActivities = budgetActivities;
    }

    public Double getBudgetShopping() {
        return budgetShopping;
    }

    public void setBudgetShopping(Double budgetShopping) {
        this.budgetShopping = budgetShopping;
    }

    public Double getBudgetOther() {
        return budgetOther;
    }

    public void setBudgetOther(Double budgetOther) {
        this.budgetOther = budgetOther;
    }

    // Transportation getters and setters
    public String getTransportPrimary() {
        return transportPrimary;
    }

    public void setTransportPrimary(String transportPrimary) {
        this.transportPrimary = transportPrimary;
    }

    public String getTransportSecondary() {
        return transportSecondary;
    }

    public void setTransportSecondary(String transportSecondary) {
        this.transportSecondary = transportSecondary;
    }

    public String getTransportNotes() {
        return transportNotes;
    }

    public void setTransportNotes(String transportNotes) {
        this.transportNotes = transportNotes;
    }

    public List<TripDay> getDays() {
        return days;
    }

    public void setDays(List<TripDay> days) {
        this.days = days;
    }
}
