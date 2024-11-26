package com.example.capstone1.repository;

import com.example.capstone1.model.Orders;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 사용자별 주문 조회
    List<Orders> findByUser(User user);

    // 특정 매장별 주문 조회
    List<Orders> findByStore(Store store);

    // 만료되지 않은 주문 중 만료 시간이 지난 주문 조회
    @Query("SELECT o FROM Orders o WHERE o.expireTime <= :now AND o.expired = false")
    List<Orders> findExpiredOrders(LocalDateTime now);
}