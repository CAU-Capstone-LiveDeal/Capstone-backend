package com.example.capstone1.scheduler;

import com.example.capstone1.service.DiscountService;
import com.example.capstone1.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiscountScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DiscountScheduler.class);

    private final DiscountService discountService;
    private final NotificationService notificationService;

    public DiscountScheduler(DiscountService discountService, NotificationService notificationService) {
        this.discountService = discountService;
        this.notificationService = notificationService;
    }

    // 매 분마다 할인 활성화 및 알림 발송 체크
    @Scheduled(fixedRate = 60000)
    public void activateDiscountsAndNotifyUsers() {
        try {
            logger.info("할인 활성화 작업 시작");
            discountService.activateDiscounts();
            logger.info("활성화된 할인에 대해 사용자 알림 발송 시작");
            notificationService.notifyUsersAboutDiscounts();
            logger.info("사용자 알림 발송 완료");
        } catch (Exception e) {
            logger.error("할인 활성화 및 알림 발송 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    // 매 분마다 할인 비활성화 체크
    @Scheduled(fixedRate = 60000)
    public void deactivateExpiredDiscounts() {
        try {
            logger.info("만료된 할인 비활성화 작업 시작");
            discountService.deactivateExpiredDiscounts();
            logger.info("만료된 할인 비활성화 작업 완료");
        } catch (Exception e) {
            logger.error("할인 비활성화 작업 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}