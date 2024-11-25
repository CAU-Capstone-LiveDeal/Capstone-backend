package com.example.capstone1.service;

import com.example.capstone1.model.Menu;
import com.example.capstone1.model.Orders;
import com.example.capstone1.model.Store;
import com.example.capstone1.repository.MenuRepository;
import com.example.capstone1.repository.OrderRepository;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    // 주문 생성
    public Orders createOrder(Long storeId, Long menuId, Integer quantity) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if (store.getEmptyTables() <= 0) {
            throw new IllegalStateException("No empty tables available for order");
        }

        Orders order = new Orders();
        order.setStore(store);
        order.setMenu(menu);
        order.setQuantity(quantity);
        order.setOrderTime(LocalDateTime.now());
        order.setExpireTime(LocalDateTime.now().plusMinutes(30)); // 30분 후 만료
        order.setExpired(false); // 초기 상태는 만료되지 않음

        store.decreaseEmptyTables(); // 빈 테이블 감소
        storeRepository.save(store); // 변경 사항 저장

        return orderRepository.save(order);
    }

    // 만료된 주문 처리
    public void processExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();
        // JPQL 기반의 메서드 호출
        List<Orders> expiredOrders = orderRepository.findExpiredOrders(now);

        for (Orders order : expiredOrders) {
            Store store = order.getStore();
            store.increaseEmptyTables(); // 빈 테이블 증가
            storeRepository.save(store);

            order.setExpired(true); // 주문을 만료 상태로 변경
            orderRepository.save(order);
        }
    }

    // 전체 주문 조회
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // 특정 주문 조회
    public Orders getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    // 특정 매장의 모든 주문 조회
    public List<Orders> getOrdersByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        return orderRepository.findByStore(store);
    }

    // 주문 수정
    public Orders updateOrder(Long orderId, Integer quantity) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setQuantity(quantity);
        return orderRepository.save(order);
    }

    // 주문 삭제
    public void deleteOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Store store = order.getStore();
        store.increaseEmptyTables(); // 빈 테이블 복원
        storeRepository.save(store);

        orderRepository.delete(order);
    }
}