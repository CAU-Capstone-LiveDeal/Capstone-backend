//package com.example.capstone1;
//
//import com.example.capstone1.model.*;
//import com.example.capstone1.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class DummyDataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private MenuRepository menuRepository;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private DiscountRepository discountRepository;
//
//    @Autowired
//    private OrdersRepository ordersRepository;
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    private final Random random = new Random();
//
//    private final String[] categories = {"한식", "중식", "양식", "일식"};
//    private final String[][] menuItems = {
//            {"순대국밥", "김치찌개", "된장찌개", "불고기", "비빔밥", "칼국수"},
//            {"짜장면", "짬뽕", "탕수육", "마파두부", "군만두", "볶음밥"},
//            {"파스타", "스테이크", "리조또", "피자", "버거", "샐러드"},
//            {"초밥", "사시미", "우동", "라멘", "덴푸라", "돈까스"}
//    };
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 관리자 계정 생성
//        List<User> adminUsers = new ArrayList<>();
//        for (int i = 1; i <= 20; i++) {
//            User admin = new User(
//                    "admin" + i,
//                    "password",
//                    "ADMIN",
//                    "Admin Address " + i,
//                    randomLatitude(),
//                    randomLongitude()
//            );
//            adminUsers.add(userRepository.save(admin));
//        }
//
//        // 사용자 계정 생성
//        List<User> normalUsers = new ArrayList<>();
//        for (int i = 1; i <= 20; i++) {
//            User user = new User(
//                    "user" + i,
//                    "password",
//                    "USER",
//                    "User Address " + i,
//                    randomLatitude(),
//                    randomLongitude()
//            );
//            normalUsers.add(userRepository.save(user));
//        }
//
//        // 매장 및 메뉴 생성
//        List<Store> stores = new ArrayList<>();
//        for (int i = 0; i < adminUsers.size(); i++) {
//            String category = categories[i % categories.length];
//            User owner = adminUsers.get(i);
//
//            Store store = new Store(
//                    category + " Store " + (i + 1),
//                    "Store Address " + (i + 1),
//                    "010-1234-567" + i,
//                    category,
//                    randomLatitude(),
//                    randomLongitude(),
//                    owner,
//                    20,
//                    random.nextInt(20) // 빈 테이블 수
//            );
//            store = storeRepository.save(store);
//            stores.add(store);
//
//            // 메뉴 생성
//            for (int j = 0; j < menuItems[i % categories.length].length; j++) {
//                Menu menu = new Menu(store, menuItems[i % categories.length][j], 10000.0 + random.nextInt(5000));
//                menuRepository.save(menu);
//            }
//        }
//
//        // 리뷰 생성
//        for (User user : normalUsers) {
//            for (Store store : stores) {
//                String reviewContent = String.format(
//                        "맛: %s, 서비스: %s, 청결도: %s, 인테리어: %s",
//                        randomComment("훌륭하다", "좋다", "괜찮다", "별로다"),
//                        randomComment("친절하다", "보통이다", "불친절하다", "최악이다"),
//                        randomComment("깨끗하다", "보통이다", "더럽다", "청소 필요"),
//                        randomComment("아늑하다", "깔끔하다", "보통이다", "지저분하다")
//                );
//                Review review = new Review(
//                        reviewContent,
//                        random.nextInt(5) + 1, // 평점 (1~5)
//                        user,
//                        store
//                );
//                reviewRepository.save(review);
//            }
//        }
//
//        // 할인 생성
//        for (Store store : stores) {
//            List<Menu> menus = store.getMenus();
//            for (int i = 0; i < menus.size(); i++) {
//                if (i % 2 == 0) { // 일부 메뉴에만 할인 적용
//                    Menu menu = menus.get(i);
//                    Discount discount = new Discount();
//                    discount.setMenu(menu);
//                    discount.setDiscountRate(i % 4 == 0 ? 0.2 : 0.4);
//                    discount.setStartTime(LocalDateTime.now());
//                    discount.setEndTime(LocalDateTime.now().plusDays(1));
//                    discountRepository.save(discount);
//                }
//            }
//        }
//
//        // 주문 생성
//        for (User user : normalUsers) {
//            for (Store store : stores) {
//                Orders order = new Orders();
//                order.setUser(user);
//                order.setStore(store);
//                order.setOrderTime(LocalDateTime.now());
//                order.setExpireTime(LocalDateTime.now().plusHours(1));
//                ordersRepository.save(order);
//
//                // 주문 항목 추가
//                for (Menu menu : store.getMenus()) {
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setOrders(order);
//                    orderItem.setMenu(menu);
//                    orderItem.setQuantity(random.nextInt(5) + 1);
//                    orderItemRepository.save(orderItem);
//                }
//            }
//        }
//    }
//
//    private Double randomLatitude() {
//        double latitude = 37.507513 + (random.nextDouble() * 0.01) - 0.005;
//        return formatToSixDecimalPlaces(latitude);
//    }
//
//    private Double randomLongitude() {
//        double longitude = 126.958696 + (random.nextDouble() * 0.01) - 0.005;
//        return formatToSixDecimalPlaces(longitude);
//    }
//
//    private Double formatToSixDecimalPlaces(double value) {
//        // String.format으로 소수점 6자리까지 강제로 출력 후 Double로 변환
//        return Double.valueOf(String.format("%.6f", value));
//    }
//
//    private String randomComment(String... options) {
//        return options[random.nextInt(options.length)];
//    }
//}