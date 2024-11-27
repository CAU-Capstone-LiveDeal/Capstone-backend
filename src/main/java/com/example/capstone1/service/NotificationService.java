package com.example.capstone1.service;

import com.example.capstone1.model.Discount;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.DiscountRepository;
import com.example.capstone1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;

    public NotificationService(DiscountRepository discountRepository, UserRepository userRepository) {
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
    }

    public void notifyUsersAboutDiscounts() {
        // 현재 활성화된 할인 목록 조회
        List<Discount> activeDiscounts = discountRepository.findByActiveTrue();

        for (Discount discount : activeDiscounts) {
            Store store = discount.getMenu().getStore();

            // 매장 근처의 사용자 조회 (예시: 반경 1km 이내)
            List<User> nearbyUsers = userRepository.findUsersNearStore(store.getLatitude(), store.getLongitude(), 1.0);

            for (User user : nearbyUsers) {
                // 사용자에게 알림 발송 (구현은 예시로 표시)
                sendNotification(user, discount);
            }
        }
    }

    private void sendNotification(User user, Discount discount) {
        // 알림 발송 로직 구현 (예: 이메일, 푸시 알림 등)
        System.out.println("사용자 " + user.getUsername() + "에게 할인 알림을 발송했습니다.");
    }
}