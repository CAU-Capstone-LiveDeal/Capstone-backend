package com.example.capstone1.service;

import com.example.capstone1.dto.RecommendationRequestDTO;
import com.example.capstone1.dto.RecommendationResponseDTO;
import com.example.capstone1.dto.StoreAnalysisDTO;
import com.example.capstone1.model.ReviewAnalysisScore;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.ReviewAnalysisScoreRepository;
import com.example.capstone1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewAnalysisScoreRepository analysisScoreRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Long> getTop5Stores(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 모든 매장의 분석 점수 가져오기
        List<ReviewAnalysisScore> analysisScores = analysisScoreRepository.findAll();

        // 요청 DTO 생성
        RecommendationRequestDTO requestDTO = new RecommendationRequestDTO();
        requestDTO.setPreferences(user.getPreferences());
        requestDTO.setImportance(user.getImportance());
        requestDTO.setStoreAnalyses(
                analysisScores.stream().map(score -> {
                    StoreAnalysisDTO dto = new StoreAnalysisDTO();
                    dto.setStoreId(score.getStore().getId());
                    dto.setTaste(score.getTaste());
                    dto.setService(score.getService());
                    dto.setInterior(score.getInterior());
                    dto.setCleanliness(score.getCleanliness());
                    return dto;
                }).collect(Collectors.toList())
        );

        // AI 서버에 요청
        String aiServerUrl = "http://ai-server-url/get-top-stores"; // 실제 AI 서버 URL로 변경
        RecommendationResponseDTO response = restTemplate.postForObject(
                aiServerUrl,
                requestDTO,
                RecommendationResponseDTO.class
        );

        return response.getRecommendedStoreIds();
    }
}