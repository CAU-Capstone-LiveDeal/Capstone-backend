package com.example.capstone1.repository;

import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 추가적인 메소드가 필요할 경우 여기에 정의
    List<Store> findByOwner(User owner);
}