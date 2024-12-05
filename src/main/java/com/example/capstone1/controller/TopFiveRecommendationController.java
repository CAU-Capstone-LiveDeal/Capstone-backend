package com.example.capstone1.controller;

import com.example.capstone1.dto.TopFiveRecommendationResponseDTO;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.UserRepository;
import com.example.capstone1.service.TopFiveRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/top5recommendations")
public class TopFiveRecommendationController {

    @Autowired
    private TopFiveRecommendationService topFiveRecommendationService;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    /**
     * 탑5 매장 추천을 요청하고 응답을 반환합니다.
     *
     * @param authentication 인증된 사용자 정보
     * @return 추천된 매장 ID 리스트
     */
    @PostMapping
    public ResponseEntity<TopFiveRecommendationResponseDTO> getTopFiveRecommendations(
            Authentication authentication) {
        // 인증된 사용자의 이름을 가져옵니다.
        String username = authentication.getName();

        // 사용자 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        // 사용자 ID 가져오기
        Long userId = user.getId();

        // 탑5 추천 조회
        TopFiveRecommendationResponseDTO responseDTO = topFiveRecommendationService.getTopFiveRecommendations(userId);

        // 프론트엔드에 반환
        return ResponseEntity.ok(responseDTO);
    }
}