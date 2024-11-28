package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendationRequestDTO {
    private String preferences;
    private String importance;
    private List<StoreAnalysisDTO> storeAnalyses;
}

