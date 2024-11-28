package com.example.capstone1.service;

import com.example.capstone1.dto.ReviewSuggestionResponseDTO;
import com.example.capstone1.model.ImportantReview;
import com.example.capstone1.model.ReviewAnalysisSuggestion;
import com.example.capstone1.model.Review;
import com.example.capstone1.model.Store;
import com.example.capstone1.repository.ReviewAnalysisSuggestionRepository;
import com.example.capstone1.repository.ReviewRepository;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 리뷰 분석 제안을 처리하는 서비스.
 */
@Service
public class ReviewSuggestionService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewAnalysisSuggestionRepository suggestionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public ReviewAnalysisSuggestion getSuggestions(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // 매장의 모든 리뷰를 가져옴
        List<String> reviews = reviewRepository.findByStore(store).stream()
                .map(Review::getContent)
                .collect(Collectors.toList());

        // 요청 DTO 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("storeId", storeId);
        requestBody.put("reviews", reviews);

        // AI 서버에 요청
        String aiServerUrl = "http://ai-server-url/get-review-suggestions"; // 실제 AI 서버 URL로 변경
        ReviewSuggestionResponseDTO response = restTemplate.postForObject(
                aiServerUrl,
                requestBody,
                ReviewSuggestionResponseDTO.class
        );

        // 결과 저장
        ReviewAnalysisSuggestion suggestion = new ReviewAnalysisSuggestion();
        suggestion.setStore(store);
        suggestion.setSummary(response.getSummary());
        suggestion.setAnalyzedAt(LocalDateTime.now());

        // Map<String, List<String>> -> List<ImportantReview> 변환
        List<ImportantReview> importantReviews = response.getImportantReviews().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(review -> {
                            ImportantReview importantReview = new ImportantReview();
                            importantReview.setCategory(entry.getKey());
                            importantReview.setReview(review);
                            importantReview.setSuggestion(suggestion);
                            return importantReview;
                        }))
                .collect(Collectors.toList());

        suggestion.setImportantReviews(importantReviews);

        suggestionRepository.save(suggestion);

        return suggestion;
    }

    public ReviewSuggestionResponseDTO getReviewSuggestionResponse(Long storeId) {
        ReviewAnalysisSuggestion suggestion = getSuggestions(storeId);

        // 응답 DTO 생성
        ReviewSuggestionResponseDTO responseDTO = new ReviewSuggestionResponseDTO();
        responseDTO.setStoreId(suggestion.getStore().getId());
        responseDTO.setSummary(suggestion.getSummary());

        // List<ImportantReview> -> Map<String, List<String>> 변환
        Map<String, List<String>> importantReviewsMap = suggestion.getImportantReviews().stream()
                .collect(Collectors.groupingBy(
                        ImportantReview::getCategory,
                        Collectors.mapping(ImportantReview::getReview, Collectors.toList())
                ));

        responseDTO.setImportantReviews(importantReviewsMap);

        return responseDTO;
    }
}