// Update RecommendationController.java if needed
package com.example.capstone1.controller;

import com.example.capstone1.dto.AnalysisScoreDTO;
import com.example.capstone1.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendationscore")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    /**
     * 주어진 storeId에 대한 추천 점수를 반환합니다.
     *
     * @param storeId 분석할 매장 ID
     * @param authentication 인증된 사용자 정보
     * @return 분석 점수 리스트
     */
    @PostMapping
    public ResponseEntity<List<AnalysisScoreDTO>> getRecommendations(
            @RequestParam Long storeId,
            Authentication authentication) {
        // 인증된 사용자의 이름을 필요로 할 경우 사용
        String username = authentication.getName();

        // 추천 점수 조회
        List<AnalysisScoreDTO> analysisScores = recommendationService.getRecommendations(storeId);

        // 프론트엔드에 반환
        return ResponseEntity.ok(analysisScores);
    }
}