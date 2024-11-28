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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // AI 서버에 요청
        String aiServerUrl = "http://ai-server-url/get-review-suggestions"; // 실제 AI 서버 URL로 변경
        ReviewSuggestionResponseDTO response = restTemplate.postForObject(
                aiServerUrl,
                reviews,
                ReviewSuggestionResponseDTO.class
        );

        // Map<String, List<String>> -> List<ImportantReview> 변환
        List<ImportantReview> importantReviews = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : response.getImportantReviews().entrySet()) {
            String category = entry.getKey();
            for (String review : entry.getValue()) {
                ImportantReview importantReview = new ImportantReview();
                importantReview.setCategory(category);
                importantReview.setReview(review);
                importantReviews.add(importantReview);
            }
        }

        // 결과 저장
        ReviewAnalysisSuggestion suggestion = new ReviewAnalysisSuggestion();
        suggestion.setStore(store);
        suggestion.setImportantReviews(importantReviews); // 변환된 List<ImportantReview> 설정
        suggestion.setSummary(response.getSummary());
        suggestion.setAnalyzedAt(LocalDateTime.now());

        // 중요 리뷰와 관계 설정
        for (ImportantReview review : importantReviews) {
            review.setSuggestion(suggestion);
        }

        return suggestionRepository.save(suggestion);
    }
}