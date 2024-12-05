// Define StoreAnalysisScoreRepository.java if not already defined
package com.example.capstone1.repository;

import com.example.capstone1.model.StoreAnalysisScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreAnalysisScoreRepository extends JpaRepository<StoreAnalysisScore, Long> {
    List<StoreAnalysisScore> findByStoreId(Long storeId);
}