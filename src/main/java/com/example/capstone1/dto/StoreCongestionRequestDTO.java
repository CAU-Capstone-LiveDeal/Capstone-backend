package com.example.capstone1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StoreCongestionRequestDTO {

    @NotNull
    private Long storeId;

    @Min(11)
    @Max(20)
    private int timeSlot;

    @Min(0)
    @Max(4)
    private int congestionLevel;

    // Getters and Setters
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public int getTimeSlot() { return timeSlot; }
    public void setTimeSlot(int timeSlot) { this.timeSlot = timeSlot; }

    public int getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(int congestionLevel) { this.congestionLevel = congestionLevel; }
}