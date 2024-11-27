package com.example.capstone1.scheduler;

import com.example.capstone1.service.DiscountService;
import com.example.capstone1.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiscountScheduler {

    private final DiscountService discountService;
    private final NotificationService notificationService;

    public DiscountScheduler(DiscountService discountService, NotificationService notificationService) {
        this.discountService = discountService;
        this.notificationService = notificationService;
    }

    // 매 분마다 할인 활성화 및 알림 발송 체크
    @Scheduled(fixedRate = 60000)
    public void activateDiscountsAndNotifyUsers() {
        discountService.activateDiscounts();

        // 활성화된 할인에 대해 사용자에게 알림 발송
        notificationService.notifyUsersAboutDiscounts();
    }

    // 매 분마다 할인 비활성화 체크
    @Scheduled(fixedRate = 60000)
    public void deactivateExpiredDiscounts() {
        discountService.deactivateExpiredDiscounts();
    }
}