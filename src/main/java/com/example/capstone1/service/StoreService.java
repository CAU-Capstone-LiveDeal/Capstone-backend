package com.example.capstone1.service;

import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반지름 (킬로미터)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon1 - lon2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (킬로미터)
    }

    public List<Store> findStoresWithinRadius(double userLat, double userLon, double radius) {
        List<Store> allStores = findAllStores();
        List<Store> storesWithinRadius = new ArrayList<>();

        for (Store store : allStores) {
            double distance = haversine(userLat, userLon, store.getLatitude(), store.getLongitude());
            if (distance <= radius) {
                storesWithinRadius.add(store);
            }
        }
        return storesWithinRadius;
    }

    public Store updateStore(Long storeId, Store updatedStore, User currentUser) {
        Optional<Store> storeOpt = storeRepository.findById(storeId);

        if (storeOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found");
        }

        Store store = storeOpt.get();

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
}