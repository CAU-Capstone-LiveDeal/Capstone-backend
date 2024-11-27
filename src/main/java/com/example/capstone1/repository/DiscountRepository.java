package com.example.capstone1.repository;

import com.example.capstone1.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // 현재 활성화된 할인 목록 조회
    List<Discount> findByActiveTrue();

    // 특정 시간에 시작하는 할인 조회
    List<Discount> findByStartTime(LocalDateTime time);

    // 종료 시간이 지났고 활성화된 할인 조회
    List<Discount> findByEndTimeBeforeAndActiveTrue(LocalDateTime time);
}