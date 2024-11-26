package com.example.capstone1.controller;

import com.example.capstone1.dto.OrderRequestDTO;
import com.example.capstone1.dto.OrderResponseDTO;
import com.example.capstone1.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        String username = authentication.getName();
        try {
            ordersService.createOrder(orderRequestDTO, username);
            return ResponseEntity.ok("주문이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 사용자별 주문 조회
    @GetMapping("/user")
    public List<OrderResponseDTO> getOrdersByUser(Authentication authentication) {
        String username = authentication.getName();
        return ordersService.getOrdersByUser(username);
    }

    // 주문 ID로 주문 조회
    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrderById(@PathVariable Long orderId) {
        return ordersService.getOrderById(orderId);
    }

    // 매장별 주문 조회
    @GetMapping("/store/{storeId}")
    public List<OrderResponseDTO> getOrdersByStore(@PathVariable Long storeId) {
        return ordersService.getOrdersByStore(storeId);
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public String updateOrder(@PathVariable Long orderId, @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        String username = authentication.getName();
        ordersService.updateOrder(orderId, orderRequestDTO, username);
        return "주문이 성공적으로 수정되었습니다.";
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId, Authentication authentication) {
        String username = authentication.getName();
        ordersService.deleteOrder(orderId, username);
        return "주문이 성공적으로 삭제되었습니다.";
    }
}