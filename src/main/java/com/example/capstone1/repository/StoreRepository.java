package com.example.capstone1.repository;

import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 추가적인 메소드가 필요할 경우 여기에 정의
    List<Store> findByOwner(User owner);
    @Query(value = "SELECT s.*, ST_Distance_Sphere(s.location, ST_GeomFromText(CONCAT('POINT(', :lon, ' ', :lat, ')'), 4326)) as distance " +
            "FROM store s " +
            "WHERE ST_Distance_Sphere(s.location, ST_GeomFromText(CONCAT('POINT(', :lon, ' ', :lat, ')'), 4326)) <= :radius " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Store> findStoresWithinRadius(@Param("lat") double latitude, @Param("lon") double longitude, @Param("radius") double radius);

}