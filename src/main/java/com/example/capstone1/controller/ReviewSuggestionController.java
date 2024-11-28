package com.example.capstone1.controller;

import com.example.capstone1.dto.ReviewSuggestionResponseDTO;
import com.example.capstone1.service.ReviewSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 리뷰 제안 요청을 처리하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/suggestions")
public class ReviewSuggestionController {

    @Autowired
    private ReviewSuggestionService suggestionService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ReviewSuggestionResponseDTO> getReviewSuggestions(@PathVariable Long storeId) {
        ReviewSuggestionResponseDTO responseDTO = suggestionService.getReviewSuggestionResponse(storeId);
        return ResponseEntity.ok(responseDTO);
    }
}