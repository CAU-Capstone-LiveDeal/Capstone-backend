package com.example.capstone1.repository;

import com.example.capstone1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // 매장 근처의 사용자 조회 (위도, 경도, 반경)
    @Query("SELECT u FROM User u WHERE FUNCTION('calculate_distance', u.latitude, u.longitude, :storeLat, :storeLng) <= :radius")
    List<User> findUsersNearStore(double storeLat, double storeLng, double radius);
}