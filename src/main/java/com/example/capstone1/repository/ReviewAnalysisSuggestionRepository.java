package com.example.capstone1.repository;

import com.example.capstone1.model.ReviewAnalysisSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewAnalysisSuggestionRepository extends JpaRepository<ReviewAnalysisSuggestion, Long> {
    Optional<ReviewAnalysisSuggestion> findByStoreId(Long storeId);
}