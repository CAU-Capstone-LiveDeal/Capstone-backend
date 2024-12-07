package com.example.capstone1.service;

import com.example.capstone1.dto.StoreRecommendationDetailDTO;
import com.example.capstone1.dto.TopFiveRecommendationRequestDTO;
import com.example.capstone1.dto.TopFiveRecommendationResponseDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.StoreAnalysisScore;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.StoreAnalysisScoreRepository;
import com.example.capstone1.repository.StoreRepository;
import com.example.capstone1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TopFiveRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(TopFiveRecommendationService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreAnalysisScoreRepository storeAnalysisScoreRepository;

    @Autowired
    private StoreRepository storeRepository; // StoreRepository 주입

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper를 사용하여 JSON 변환

    @Transactional
    public TopFiveRecommendationResponseDTO getTopFiveRecommendations(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 사용자 선호도와 중요도 가져오기
        String preferType = user.getPreferences(); // User 엔티티의 importance 필드
        String preferCategories = user.getImportance(); // User 엔티티의 preferences 필드

        // StoreAnalysisScore 조회 (모든 매장 분석 점수)
        List<StoreAnalysisScore> analysisScores = storeAnalysisScoreRepository.findAll();

        if (analysisScores.isEmpty()) {
            throw new IllegalStateException("No store analysis scores available for recommendations.");
        }

        // StoreAnalysisScore를 StoreRecommendationDetailDTO로 매핑
        List<StoreRecommendationDetailDTO> storeList = analysisScores.stream().map(score -> {
            StoreRecommendationDetailDTO dto = new StoreRecommendationDetailDTO();
            dto.setStoreId(score.getStoreId());
            dto.setStoreType(getStoreCategoryById(score.getStoreId())); // 매장 카테고리 가져오기
            dto.setRatingscore(score.getRatingscore());
            dto.setTaste(score.getTaste());
            dto.setService(score.getService());
            dto.setInterior(score.getInterior());
            dto.setCleanliness(score.getCleanliness());
            return dto;
        }).collect(Collectors.toList());

        // TopFiveRecommendationRequestDTO 구성
        TopFiveRecommendationRequestDTO requestDTO = new TopFiveRecommendationRequestDTO();
        requestDTO.setPreferType(Collections.singletonList(preferType));
        requestDTO.setPreferCategories(preferCategories);
        requestDTO.setStorelist(storeList);

        try {
            // JSON 형식으로 요청 데이터 로깅
            String requestJson = objectMapper.writeValueAsString(requestDTO);
            logger.debug("Request to AI server: {}", requestJson);

            // AI 서버 URL
            String aiServerUrl = "https://d656-49-142-59-70.ngrok-free.app/recommend/recommend";

            // 응답 타입 정의
            ParameterizedTypeReference<TopFiveRecommendationResponseDTO> responseType =
                    new ParameterizedTypeReference<TopFiveRecommendationResponseDTO>() {};

            // HTTP 요청 엔티티 생성
            HttpEntity<TopFiveRecommendationRequestDTO> requestEntity = new HttpEntity<>(requestDTO);

            // AI 서버에 POST 요청
            TopFiveRecommendationResponseDTO responseDTO = restTemplate.exchange(
                    aiServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            ).getBody();

            // 응답 데이터 로깅
            logger.debug("Received top5 recommendation response from AI server: {}", responseDTO);

            if (responseDTO == null || responseDTO.getRecommendedStoreIds().isEmpty()) {
                throw new IllegalStateException("AI server returned empty top5 recommendations");
            }

            return responseDTO;

        } catch (HttpClientErrorException e) {
            // AI 서버의 오류 처리 및 로깅
            logger.error("Failed to call AI server for top5 recommendations: {}", e.getStatusCode());
            logger.error("AI server response: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to call AI server for top5 recommendations: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // 일반 예외 처리 및 로깅
            logger.error("An error occurred while getting top5 recommendations", e);
            throw new RuntimeException("An error occurred while getting top5 recommendations", e);
        }
    }

    private String getStoreCategoryById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
        return store.getCategory();
    }
}