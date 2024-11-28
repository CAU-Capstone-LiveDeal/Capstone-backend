package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * AI 서버에서 리뷰 분석 응답을 받을 때 사용하는 DTO.
 */
@Getter
@Setter
public class ReviewAnalysisResponseDTO {
    private Long storeId;
    private int taste;
    private int service;
    private int interior;
    private int cleanliness;
}