package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendationResponseDTO {
    private List<Long> recommendedStoreIds;
}