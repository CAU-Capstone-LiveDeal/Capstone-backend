package com.example.capstone1.service;

import com.example.capstone1.dto.StoreDTO;
import com.example.capstone1.dto.StoreRequestDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Store saveStore(StoreRequestDTO storeRequestDTO, User owner) {
        Store store = mapToEntity(storeRequestDTO);
        store.setOwner(owner);
        store.setCongestionLevel(store.calculateCongestionLevel());
        store.setDiscountActive(false); // 초기 할인 상태 설정
        return storeRepository.save(store);
    }

    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> findById(Long storeId) {
        return storeRepository.findById(storeId);
    }

    public List<Store> findStoresWithinRadius(double userLat, double userLon, double radius) {
        return storeRepository.findAll().stream()
                .filter(store -> haversine(userLat, userLon, store.getLatitude(), store.getLongitude()) <= radius)
                .collect(Collectors.toList());
    }

    public List<Store> findStoresByOwner(User owner) {
        return storeRepository.findByOwner(owner);
    }

    public Store updateStore(Long storeId, StoreDTO updatedStoreDTO, User currentUser) {
        Store store = findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
        if (!store.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to update this store.");
        }

        store.setName(updatedStoreDTO.getName());
        store.setAddress(updatedStoreDTO.getAddress());
        store.setPhone(updatedStoreDTO.getPhone());
        store.setCategory(updatedStoreDTO.getCategory());
        store.setLatitude(updatedStoreDTO.getLatitude());
        store.setLongitude(updatedStoreDTO.getLongitude());
        store.setTotalTables(updatedStoreDTO.getTotalTables());
        store.setEmptyTables(updatedStoreDTO.getEmptyTables());
        store.setCongestionLevel(store.calculateCongestionLevel());

        return storeRepository.save(store);
    }

    public StoreDTO mapToStoreDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setAddress(store.getAddress());
        dto.setPhone(store.getPhone());
        dto.setCategory(store.getCategory());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        dto.setTotalTables(store.getTotalTables());
        dto.setEmptyTables(store.getEmptyTables());
        dto.setCongestionLevel(store.getCongestionLevel());
        dto.setDiscountActive(store.isDiscountActive());
        dto.setAverageRating(store.getAverageRating()); // 평균 점수 추가
        return dto;
    }

    private Store mapToEntity(StoreRequestDTO storeRequestDTO) {
        Store store = new Store();
        store.setName(storeRequestDTO.getName());
        store.setAddress(storeRequestDTO.getAddress());
        store.setPhone(storeRequestDTO.getPhone());
        store.setCategory(storeRequestDTO.getCategory());
        store.setLatitude(storeRequestDTO.getLatitude());
        store.setLongitude(storeRequestDTO.getLongitude());
        store.setTotalTables(storeRequestDTO.getTotalTables());
        store.setEmptyTables(storeRequestDTO.getEmptyTables());
        return store;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon1 - lon2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}