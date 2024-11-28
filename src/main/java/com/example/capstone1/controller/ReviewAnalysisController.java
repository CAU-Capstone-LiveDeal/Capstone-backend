package com.example.capstone1.controller;

import com.example.capstone1.dto.ReviewAnalysisResponseDTO;
import com.example.capstone1.service.ReviewAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 리뷰 분석 요청을 처리하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/analysis")
public class ReviewAnalysisController {

    @Autowired
    private ReviewAnalysisService analysisService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ReviewAnalysisResponseDTO> getReviewAnalysis(@PathVariable Long storeId) {
        ReviewAnalysisResponseDTO responseDTO = analysisService.getReviewAnalysisResponse(storeId);
        return ResponseEntity.ok(responseDTO);
    }
}