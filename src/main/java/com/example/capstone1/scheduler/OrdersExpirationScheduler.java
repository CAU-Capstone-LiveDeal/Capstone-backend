package com.example.capstone1.scheduler;

import com.example.capstone1.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrdersExpirationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrdersExpirationScheduler.class);

    private final OrdersService ordersService;

    public OrdersExpirationScheduler(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // 1분마다 주문 만료 처리
    @Scheduled(fixedRate = 60000)
    public void expireOrders() {
        try {
            ordersService.expireOrders();
            logger.info("주문 만료 작업이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            logger.error("주문 만료 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}