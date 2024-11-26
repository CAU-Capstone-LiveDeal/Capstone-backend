package com.example.capstone1.service;

import com.example.capstone1.dto.ReviewDTO;
import com.example.capstone1.dto.StoreDTO;
import com.example.capstone1.model.Review;
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

    public Store saveStore(Store store) {
        if (store.getAddress() == null || store.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Store address must not be null or empty");
        }
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

    public Store updateStore(Long storeId, Store updatedStore, User currentUser) {
        Store store = findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
        if (!store.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to update this store.");
        }

        store.setName(updatedStore.getName());
        store.setAddress(updatedStore.getAddress());
        store.setPhone(updatedStore.getPhone());
        store.setCategory(updatedStore.getCategory());
        store.setLatitude(updatedStore.getLatitude());
        store.setLongitude(updatedStore.getLongitude());

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
        dto.setReviews(store.getReviews().stream()
                .map(this::mapToReviewDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private ReviewDTO mapToReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setAuthorUsername(review.getAuthor().getUsername());
        return dto;
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