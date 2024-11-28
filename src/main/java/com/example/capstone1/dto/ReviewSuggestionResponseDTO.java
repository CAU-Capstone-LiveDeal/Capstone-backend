package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReviewSuggestionResponseDTO {
    private Map<String, List<String>> importantReviews; // taste, service 등
    private String summary;
}