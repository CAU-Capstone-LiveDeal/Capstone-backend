package com.example.capstone1.controller;

import com.example.capstone1.model.Orders;
import com.example.capstone1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 주문 생성
    @PostMapping("/")
    public ResponseEntity<?> createOrder(
            @RequestParam Long storeId,
            @RequestParam Long menuId,
            @RequestParam Integer quantity) {
        try {
            Orders order = orderService.createOrder(storeId, menuId, quantity);
            return ResponseEntity.ok("Order created successfully: " + order.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전체 주문 조회
    @GetMapping("/")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Orders order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 특정 매장의 모든 주문 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Orders>> getOrdersByStoreId(@PathVariable Long storeId) {
        List<Orders> orders = orderService.getOrdersByStoreId(storeId);
        return ResponseEntity.ok(orders);
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(
            @PathVariable Long orderId,
            @RequestParam Integer quantity) {
        try {
            Orders updatedOrder = orderService.updateOrder(orderId, quantity);
            return ResponseEntity.ok("Order updated successfully: " + updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Order deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}