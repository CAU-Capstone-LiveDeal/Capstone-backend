package com.example.capstone1.controller;

import com.example.capstone1.dto.StoreDTO;
import com.example.capstone1.dto.StoreRequestDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStore(@RequestBody StoreRequestDTO storeRequestDTO) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            Store store = storeService.saveStore(storeRequestDTO, currentUser);
            return ResponseEntity.ok(storeService.mapToStoreDTO(store));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        List<StoreDTO> stores = storeService.findAllStores().stream()
                .map(storeService::mapToStoreDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<StoreDTO>> getNearbyStores(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        List<StoreDTO> nearbyStores = storeService.findStoresWithinRadius(latitude, longitude, radius).stream()
                .map(storeService::mapToStoreDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(nearbyStores);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<?> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreDTO updatedStoreDTO) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            Store store = storeService.updateStore(storeId, updatedStoreDTO, currentUser);
            return ResponseEntity.ok(storeService.mapToStoreDTO(store));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{storeId}/congestion")
    public ResponseEntity<?> getCongestionLevel(@PathVariable Long storeId) {
        try {
            Store store = storeService.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));
            return ResponseEntity.ok("Current congestion level: " + store.getCongestionLevel());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

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

    @GetMapping("/my-store-id")
    public ResponseEntity<?> getMyStoreId() {
        try {
            // 현재 인증된 사용자의 사용자명 가져오기
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User currentUser = userService.findByUsername(username);

            // 현재 사용자가 소유한 매장 조회
            List<Store> userStores = storeService.findStoresByOwner(currentUser);

            // 매장이 없을 경우 예외 처리
            if (userStores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stores registered by the user.");
            }

            // 매장의 ID 목록 반환
            List<Long> storeIds = userStores.stream().map(Store::getId).collect(Collectors.toList());
            return ResponseEntity.ok(storeIds);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}