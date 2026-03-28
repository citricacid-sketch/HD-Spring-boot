package com.travel.dto;

import java.util.List;

public class TravelPlan {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String status; // "ongoing", "completed", "draft"
    private String source; // "ai", "manual"
    private List<Day> days;
    private Budget budget;
    private Transportation transportation;
    private String description;

    // Constructors
    public TravelPlan() {
    }

    public TravelPlan(String id, String title, String startDate, String endDate, String status, String source) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.source = source;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Inner classes
    public static class Day {
        private String label;
        private String date;
        private List<Slot> slots;

        public Day() {
        }

        public Day(String label, String date, List<Slot> slots) {
            this.label = label;
            this.date = date;
            this.slots = slots;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Slot> getSlots() {
            return slots;
        }

        public void setSlots(List<Slot> slots) {
            this.slots = slots;
        }
    }

    public static class Slot {
        private String time;
        private String end;
        private String text;
        private String note;

        public Slot() {
        }

        public Slot(String time, String text) {
            this.time = time;
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class Budget {
        private double total;
        private String currency;
        private Breakdown breakdown;

        public Budget() {
        }

        public Budget(double total, String currency) {
            this.total = total;
            this.currency = currency;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Breakdown getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(Breakdown breakdown) {
            this.breakdown = breakdown;
        }

        public static class Breakdown {
            private double accommodation;
            private double transportation;
            private double food;
            private double activities;
            private double shopping;
            private double other;

            public Breakdown() {
            }

            public Breakdown(double accommodation, double transportation, double food, double activities, double shopping, double other) {
                this.accommodation = accommodation;
                this.transportation = transportation;
                this.food = food;
                this.activities = activities;
                this.shopping = shopping;
                this.other = other;
            }

            public double getAccommodation() {
                return accommodation;
            }

            public void setAccommodation(double accommodation) {
                this.accommodation = accommodation;
            }

            public double getTransportation() {
                return transportation;
            }

            public void setTransportation(double transportation) {
                this.transportation = transportation;
            }

            public double getFood() {
                return food;
            }

            public void setFood(double food) {
                this.food = food;
            }

            public double getActivities() {
                return activities;
            }

            public void setActivities(double activities) {
                this.activities = activities;
            }

            public double getShopping() {
                return shopping;
            }

            public void setShopping(double shopping) {
                this.shopping = shopping;
            }

            public double getOther() {
                return other;
            }

            public void setOther(double other) {
                this.other = other;
            }
        }
    }

    public static class Transportation {
        private String primary;
        private String secondary;
        private String notes;

        public Transportation() {
        }

        public Transportation(String primary, String secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getSecondary() {
            return secondary;
        }

        public void setSecondary(String secondary) {
            this.secondary = secondary;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}