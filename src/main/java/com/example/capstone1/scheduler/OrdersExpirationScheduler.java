package com.example.capstone1.scheduler;

import com.example.capstone1.service.OrdersService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrdersExpirationScheduler {

    private final OrdersService ordersService;

    public OrdersExpirationScheduler(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // 1분마다 주문 만료 처리
    @Scheduled(fixedRate = 60000)
    public void expireOrders() {
        ordersService.expireOrders();
    }
}