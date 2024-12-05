package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopFiveRecommendationResponseDTO {
    private List<Long> recommendedStoreIds; // 추천된 매장 ID 리스트
}