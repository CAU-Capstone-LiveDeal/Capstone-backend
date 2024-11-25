package com.example.capstone1.repository;

import com.example.capstone1.model.Menu;
import com.example.capstone1.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 매장의 모든 메뉴 조회
    List<Menu> findByStore(Store store);
}