package com.example.capstone1.controller;

import com.example.capstone1.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // 상위 매장 5개 추천
    @GetMapping("/top-stores")
    public ResponseEntity<List<Long>> getTopStores(Authentication authentication) {
        String username = authentication.getName();
        List<Long> topStores = recommendationService.getTop5Stores(username);
        return ResponseEntity.ok(topStores);
    }
}