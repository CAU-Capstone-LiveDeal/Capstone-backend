package com.example.capstone1.dto;

import java.time.LocalDate;

public class StoreCongestionResponseDTO {

    private LocalDate date;
    private int timeSlot;
    private int congestionLevel;

    // Constructor
    public StoreCongestionResponseDTO(LocalDate date, int timeSlot, int congestionLevel) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.congestionLevel = congestionLevel;
    }

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getTimeSlot() { return timeSlot; }
    public void setTimeSlot(int timeSlot) { this.timeSlot = timeSlot; }

    public int getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(int congestionLevel) { this.congestionLevel = congestionLevel; }
}