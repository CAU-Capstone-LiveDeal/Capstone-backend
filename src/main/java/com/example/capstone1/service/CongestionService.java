package com.example.capstone1.service;

import com.example.capstone1.model.Store;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CongestionService {

    @Autowired
    private StoreRepository storeRepository;

    // 혼잡도 업데이트 메서드
    public Store updateCongestionLevel(Long storeId, Integer emptyTables) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
        store.setEmptyTables(emptyTables);
        store.setCongestionLevel(store.calculateCongestionLevel());
        return storeRepository.save(store);
    }

    // 특정 매장의 혼잡도 조회
    public Integer getCongestionLevel(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
        return store.getCongestionLevel();
    }
}