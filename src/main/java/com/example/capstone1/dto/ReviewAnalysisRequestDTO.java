package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewAnalysisRequestDTO {
    private Long storeId;
    private List<String> reviews;
}