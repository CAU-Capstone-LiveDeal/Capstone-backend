package com.example.capstone1.controller;

import com.example.capstone1.model.ReviewAnalysisSuggestion;
import com.example.capstone1.service.ReviewSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suggestions")
public class ReviewSuggestionController {

    @Autowired
    private ReviewSuggestionService suggestionService;

    // 리뷰 분석 제안 요청
    @PostMapping("/store/{storeId}")
    public ResponseEntity<ReviewAnalysisSuggestion> getReviewSuggestions(@PathVariable Long storeId) {
        ReviewAnalysisSuggestion result = suggestionService.getSuggestions(storeId);
        return ResponseEntity.ok(result);
    }
}