package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class DiscountResponseDTO {
    private Long id;
    private Double percentage;
    private LocalTime startTime;
    private LocalTime endTime;
    private String menuName;
    private String storeName;
}