package com.example.capstone1.repository;

import com.example.capstone1.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 추가적인 메소드가 필요할 경우 여기에 정의
}