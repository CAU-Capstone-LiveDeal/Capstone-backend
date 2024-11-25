//package com.example.capstone1;
//
//import com.example.capstone1.model.*;
//import com.example.capstone1.repository.*;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Configuration
//public class DummyDataInitializer {
//
//    @Bean
//    CommandLineRunner initData(
//            UserRepository userRepository,
//            StoreRepository storeRepository,
//            ReviewRepository reviewRepository,
//            MenuRepository menuRepository,
//            DiscountRepository discountRepository,
//            PasswordEncoder passwordEncoder) {
//
//        return args -> {
//            Random random = new Random();
//
//            // 1. 일반 사용자 생성
//            List<User> generalUsers = new ArrayList<>();
//            for (int i = 1; i <= 100; i++) {
//                User user = new User(
//                        "user" + i,
//                        passwordEncoder.encode("password" + i),
//                        "USER",
//                        "Address " + i,
//                        random.nextDouble() * 90, // 위도 (0~90)
//                        random.nextDouble() * 180 // 경도 (0~180)
//                );
//                generalUsers.add(user);
//            }
//            userRepository.saveAll(generalUsers);
//
//            // 2. 오너 사용자 생성
//            List<User> ownerUsers = new ArrayList<>();
//            for (int i = 1; i <= 20; i++) {
//                User owner = new User(
//                        "owner" + i,
//                        passwordEncoder.encode("password" + i),
//                        "OWNER",
//                        "Owner Address " + i,
//                        random.nextDouble() * 90, // 위도 (0~90)
//                        random.nextDouble() * 180 // 경도 (0~180)
//                );
//                ownerUsers.add(owner);
//            }
//            userRepository.saveAll(ownerUsers);
//
//            // 3. 매장 생성 (각 오너당 2개)
//            List<Store> stores = new ArrayList<>();
//            for (int i = 0; i < ownerUsers.size(); i++) {
//                User owner = ownerUsers.get(i);
//                for (int j = 1; j <= 2; j++) {
//                    Store store = new Store(
//                            "Store " + (i * 2 + j),
//                            "Store Address " + (i * 2 + j),
//                            "010-1234-56" + (j + i * 2),
//                            "Category " + (j + i),
//                            random.nextDouble() * 90, // 위도 (0~90)
//                            random.nextDouble() * 180, // 경도 (0~180)
//                            owner,
//                            20, // 총 테이블 수
//                            10 // 빈 테이블 수
//                    );
//                    stores.add(store);
//                }
//            }
//            storeRepository.saveAll(stores);
//
//            // 4. 메뉴 생성 (각 매장당 3개)
//            List<Menu> menus = new ArrayList<>();
//            for (Store store : stores) {
//                for (int k = 1; k <= 3; k++) {
//                    Menu menu = new Menu(
//                            store,
//                            "Menu " + k + " for " + store.getName(),
//                            random.nextDouble() * 50 + 10 // 가격 (10~60)
//                    );
//                    menus.add(menu);
//                }
//            }
//            menuRepository.saveAll(menus);
//
//            // 5. 할인 정보 생성 (각 메뉴당 1개)
//            List<Discount> discounts = new ArrayList<>();
//            for (Menu menu : menus) {
//                Discount discount = new Discount(
//                        menu,
//                        random.nextDouble() * 30 + 10, // 할인율 (10% ~ 40%)
//                        LocalTime.parse("09:00"), // 시작 시간
//                        LocalTime.parse("21:00")  // 종료 시간
//                );
//                discounts.add(discount);
//            }
//            discountRepository.saveAll(discounts);
//
//            // 6. 리뷰 생성 (각 매장당 5개)
//            List<Review> reviews = new ArrayList<>();
//            for (Store store : stores) {
//                for (int k = 1; k <= 5; k++) {
//                    User reviewer = generalUsers.get(random.nextInt(generalUsers.size()));
//                    // 생성자 호출 부분 수정
//                    Review review = new Review(
//                            "Review content " + k + " for store " + store.getName(),
//                            random.nextInt(5) + 1, // 평점 1~5
//                            reviewer, // 리뷰 작성자
//                            store // 매장
//                    );
//                    reviews.add(review);
//                }
//            }
//            reviewRepository.saveAll(reviews);
//
//            System.out.println("Dummy data initialized successfully!");
//        };
//    }
//}