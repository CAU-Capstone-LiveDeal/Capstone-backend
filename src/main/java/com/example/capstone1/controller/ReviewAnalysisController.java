package com.example.capstone1.controller;

import com.example.capstone1.model.ReviewAnalysisScore;
import com.example.capstone1.service.ReviewAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class ReviewAnalysisController {

    @Autowired
    private ReviewAnalysisService analysisService;

    // 리뷰 분석 요청
    @PostMapping("/store/{storeId}")
    public ResponseEntity<ReviewAnalysisScore> analyzeStoreReviews(@PathVariable Long storeId) {
        ReviewAnalysisScore result = analysisService.analyzeReviews(storeId);
        return ResponseEntity.ok(result);
    }
}