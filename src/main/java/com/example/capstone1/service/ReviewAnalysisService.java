package com.example.capstone1.service;

import com.example.capstone1.dto.ReviewAnalysisRequestDTO;
import com.example.capstone1.dto.ReviewAnalysisResponseDTO;
import com.example.capstone1.model.Review;
import com.example.capstone1.model.ReviewAnalysisScore;
import com.example.capstone1.model.Store;
import com.example.capstone1.repository.ReviewAnalysisScoreRepository;
import com.example.capstone1.repository.ReviewRepository;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰를 분석하고 점수를 가져오는 서비스.
 */
@Service
public class ReviewAnalysisService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewAnalysisScoreRepository analysisScoreRepository;

    @Autowired
    private RestTemplate restTemplate;

    public ReviewAnalysisScore analyzeReviews(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));

        // 매장의 모든 리뷰를 가져옴
        List<String> reviews = reviewRepository.findByStore(store).stream()
                .map(Review::getContent)
                .collect(Collectors.toList());

        // 요청 DTO 생성
        ReviewAnalysisRequestDTO requestDTO = new ReviewAnalysisRequestDTO();
        requestDTO.setStoreId(storeId);
        requestDTO.setReviews(reviews);

        // AI 서버에 요청
        String aiServerUrl = "http://ai-server-url/analyze-reviews"; // 실제 AI 서버 URL로 변경
        ReviewAnalysisResponseDTO response = restTemplate.postForObject(
                aiServerUrl,
                requestDTO,
                ReviewAnalysisResponseDTO.class
        );

        // 분석 점수를 데이터베이스에 저장
        ReviewAnalysisScore analysisScore = new ReviewAnalysisScore();
        analysisScore.setStore(store);
        analysisScore.setTaste(response.getTaste());
        analysisScore.setService(response.getService());
        analysisScore.setInterior(response.getInterior());
        analysisScore.setCleanliness(response.getCleanliness());
        analysisScore.setAnalyzedAt(LocalDateTime.now());

        analysisScoreRepository.save(analysisScore);

        return analysisScore;
    }

    public ReviewAnalysisResponseDTO getReviewAnalysisResponse(Long storeId) {
        // 리뷰를 분석하고 점수를 가져옴
        ReviewAnalysisScore analysisScore = analyzeReviews(storeId);

        // 응답 DTO 생성
        ReviewAnalysisResponseDTO responseDTO = new ReviewAnalysisResponseDTO();
        responseDTO.setStoreId(analysisScore.getStore().getId());
        responseDTO.setTaste(analysisScore.getTaste());
        responseDTO.setService(analysisScore.getService());
        responseDTO.setInterior(analysisScore.getInterior());
        responseDTO.setCleanliness(analysisScore.getCleanliness());

        return responseDTO;
    }
}