package com.example.capstone1.repository;

import com.example.capstone1.model.Review;
import com.example.capstone1.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStore(Store store);
    // 추가된 메서드: storeId로 리뷰 조회
    List<Review> findByStoreId(Long storeId);
}