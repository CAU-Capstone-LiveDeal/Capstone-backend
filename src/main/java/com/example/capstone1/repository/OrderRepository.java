package com.example.capstone1.repository;

import com.example.capstone1.model.Orders;
import com.example.capstone1.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    // JPQL을 사용하여 만료 시간이 현재 시간 이전이고 아직 만료되지 않은 주문 조회
    @Query("SELECT o FROM Orders o WHERE o.expireTime < :now AND o.expired = false")
    List<Orders> findExpiredOrders(@Param("now") LocalDateTime now);

    // 특정 매장의 모든 주문 조회
    List<Orders> findByStore(Store store);
}