// Define StoreAnalysisScore.java if not already defined
package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class StoreAnalysisScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private int ratingscore;

    @Column(nullable = false)
    private int taste;

    @Column(nullable = false)
    private int service;

    @Column(nullable = false)
    private int interior;

    @Column(nullable = false)
    private int cleanliness;

    @Column(nullable = false)
    private LocalDateTime analyzedAt = LocalDateTime.now();
}