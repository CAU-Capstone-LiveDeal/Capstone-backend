package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * AI 서버로 리뷰 분석 요청을 보낼 때 사용하는 DTO.
 */
@Getter
@Setter
public class ReviewAnalysisRequestDTO {
    private Long storeId;
    private List<String> reviews;
}