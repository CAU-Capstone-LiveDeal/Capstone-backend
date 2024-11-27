package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountRequestDTO {

    private Long menuId;
    private Double discountRate;      // 0.0 ~ 1.0
    private LocalDateTime startTime;
    private int durationHours;        // 할인 기간 (시간 단위)
}