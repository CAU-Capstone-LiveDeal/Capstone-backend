package com.example.capstone1.repository;

import com.example.capstone1.model.ReviewAnalysisScore;
import com.example.capstone1.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewAnalysisScoreRepository extends JpaRepository<ReviewAnalysisScore, Long> {
    Optional<ReviewAnalysisScore> findByStore(Store store);
}