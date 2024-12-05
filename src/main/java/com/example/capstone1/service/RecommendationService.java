// Update RecommendationService.java to use AiReviewDTO
package com.example.capstone1.service;

import com.example.capstone1.dto.AnalysisScoreDTO;
import com.example.capstone1.dto.AiReviewDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.StoreAnalysisScore;
import com.example.capstone1.model.Review;
import com.example.capstone1.repository.StoreAnalysisScoreRepository;
import com.example.capstone1.repository.StoreRepository;
import com.example.capstone1.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StoreAnalysisScoreRepository storeAnalysisScoreRepository;

    /**
     * 주어진 storeId에 대한 추천 점수를 AI 서버에서 받아옵니다.
     *
     * @param storeId 분석할 매장 ID
     * @return 분석 점수 리스트
     */
    public List<AnalysisScoreDTO> getRecommendations(Long storeId) {
        // 매장 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));

        // 매장에 해당하는 모든 리뷰 조회
        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found for storeId: " + storeId);
        }

        // 리뷰를 AiReviewDTO로 매핑
        List<AiReviewDTO> aiReviewDTOs = reviews.stream().map(review -> {
            AiReviewDTO dto = new AiReviewDTO();
            dto.setId(review.getId());
            dto.setContent(review.getContent());
            dto.setRating(review.getRating());
            dto.setAuthorUsername(review.getAuthorUsername());
            dto.setStoreName(review.getStore().getName());
            dto.setStoreId(review.getStore().getId());
            return dto;
        }).collect(Collectors.toList());

        // 요청 데이터 로깅
        logger.debug("Sending reviews to AI server: {}", aiReviewDTOs);

        // AI 서버 URL
        String aiServerUrl = "https://0eaf-49-142-59-70.ngrok-free.app/reviewscore/scoring_reviews/"; // 실제 AI 서버 URL로 변경

        try {
            // 응답 타입 정의
            ParameterizedTypeReference<List<AnalysisScoreDTO>> responseType =
                    new ParameterizedTypeReference<List<AnalysisScoreDTO>>() {};

            // HTTP 요청 엔티티 생성
            HttpEntity<List<AiReviewDTO>> requestEntity = new HttpEntity<>(aiReviewDTOs);

            // AI 서버에 POST 요청
            List<AnalysisScoreDTO> analysisScores = restTemplate.exchange(
                    aiServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            ).getBody();

            // 응답 데이터 로깅
            logger.debug("Received analysis scores from AI server: {}", analysisScores);

            if (analysisScores == null || analysisScores.isEmpty()) {
                throw new IllegalStateException("AI server returned empty response");
            }

            // 분석 점수를 DB에 저장
            for (AnalysisScoreDTO scoreDTO : analysisScores) {
                StoreAnalysisScore storeAnalysisScore = new StoreAnalysisScore();
                storeAnalysisScore.setStoreId(scoreDTO.getStoreId());
                storeAnalysisScore.setRatingscore(scoreDTO.getRatingscore());
                storeAnalysisScore.setTaste(scoreDTO.getTaste());
                storeAnalysisScore.setService(scoreDTO.getService());
                storeAnalysisScore.setInterior(scoreDTO.getInterior());
                storeAnalysisScore.setCleanliness(scoreDTO.getCleanliness());

                storeAnalysisScoreRepository.save(storeAnalysisScore);
            }

            // 분석 점수 반환
            return analysisScores;

        } catch (HttpClientErrorException e) {
            // AI 서버의 오류 처리 및 로깅
            logger.error("Failed to call AI server: {}", e.getStatusCode());
            logger.error("AI server response: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to call AI server: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // 일반 예외 처리 및 로깅
            logger.error("An error occurred while getting recommendations for storeId: {}", storeId, e);
            throw new RuntimeException("An error occurred while getting recommendations", e);
        }
    }
}