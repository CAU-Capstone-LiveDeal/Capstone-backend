package com.example.capstone1.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class StoreCongestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private LocalDate date;
    private int timeSlot; // 11시~20시
    private int congestionLevel; // 0~4

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getTimeSlot() { return timeSlot; }
    public void setTimeSlot(int timeSlot) { this.timeSlot = timeSlot; }

    public int getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(int congestionLevel) { this.congestionLevel = congestionLevel; }
}