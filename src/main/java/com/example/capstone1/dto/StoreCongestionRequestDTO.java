package com.example.capstone1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StoreCongestionRequestDTO {

    @NotNull(message = "Store ID is required.")
    private Long storeId;

    @Min(value = 11, message = "Time slot must be between 11 and 20.")
    @Max(value = 20, message = "Time slot must be between 11 and 20.")
    private int timeSlot;

    @Min(value = 0, message = "Congestion level must be between 0 and 4.")
    @Max(value = 4, message = "Congestion level must be between 0 and 4.")
    private int congestionLevel;

    @NotNull(message = "Date is required.")
    private LocalDate date;

    // Getters and Setters
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}