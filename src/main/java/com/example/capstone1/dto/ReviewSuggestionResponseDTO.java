package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 *  AI 서버에서 리뷰 제안 응답을 받을 때 사용하는 DTO.
 */
@Getter
@Setter
public class ReviewSuggestionResponseDTO {
    private Long storeId;
    private Map<String, List<String>> importantReviews; // 카테고리별 리뷰 리스트 (예: taste, service 등)
    private String summary;
}