package com.example.capstone1.controller;

import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.service.StoreService;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStore(@RequestBody Store store) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            store.setOwner(currentUser);
            return ResponseEntity.ok(storeService.saveStore(store));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeService.findAllStores());
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Store>> getNearbyStores(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        List<Store> nearbyStores = storeService.findStoresWithinRadius(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyStores);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateStore(
            @PathVariable Long storeId,
            @RequestBody Store updatedStore) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            Store store = storeService.updateStore(storeId, updatedStore, currentUser);
            return ResponseEntity.ok(store);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 혼잡도 조회
    @GetMapping("/{storeId}/congestion")
    public ResponseEntity<?> getCongestionLevel(@PathVariable Long storeId) {
        try {
            Store store = storeService.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
            return ResponseEntity.ok("Current congestion level: " + store.getCongestionLevel());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 혼잡도 업데이트
    @PutMapping("/{storeId}/update-congestion")
    public ResponseEntity<?> updateCongestionLevel(
            @PathVariable Long storeId,
            @RequestParam Integer emptyTables) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            Store store = storeService.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));

            if (!store.getOwner().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this store.");
            }

            store.setEmptyTables(emptyTables);
            store.setCongestionLevel(store.calculateCongestionLevel());
            storeService.saveStore(store);

            return ResponseEntity.ok("Updated congestion level to: " + store.getCongestionLevel());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}