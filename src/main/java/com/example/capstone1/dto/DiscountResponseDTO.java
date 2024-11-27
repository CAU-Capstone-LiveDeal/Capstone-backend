package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountResponseDTO {

    private Long discountId;
    private Long menuId;
    private String menuName;
    private Double discountRate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
}