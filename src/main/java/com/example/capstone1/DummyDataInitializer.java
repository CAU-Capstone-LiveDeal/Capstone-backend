//package com.example.capstone1;
//
//import com.example.capstone1.model.*;
//import com.example.capstone1.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
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
//    @Autowired
//    private StoreAnalysisScoreRepository storeAnalysisScoreRepository;
//
//    @Autowired
//    private StoreCongestionRepository storeCongestionRepository;
//
//    private final Random random = new Random();
//
//    private final String[] categories = {"한식", "중식", "양식", "일식"};
//    private final Map<String, String[]> menuItemsMap = new HashMap<String, String[]>() {{
//        put("한식", new String[]{"김치찌개", "된장찌개", "불고기", "비빔밥", "칼국수", "냉면", "삼겹살", "순두부찌개", "갈비탕", "설렁탕"});
//        put("중식", new String[]{"짜장면", "짬뽕", "탕수육", "마파두부", "군만두", "볶음밥", "양장피", "고추잡채", "깐풍기", "유린기"});
//        put("양식", new String[]{"파스타", "스테이크", "리조또", "피자", "버거", "샐러드", "라자냐", "그라탕", "바베큐", "카르파초"});
//        put("일식", new String[]{"초밥", "사시미", "우동", "라멘", "텐동", "돈부리", "야키소바", "오코노미야키", "타코야키", "덴푸라"});
//    }};
//
//    private final String[] importanceOptions = {"맛", "인테리어", "청결도", "서비스"};
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 관리자 계정 생성 (50명)
//        List<User> adminUsers = new ArrayList<>();
//        for (int i = 1; i <= 50; i++) {
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
//        // 사용자 계정 생성 (500명)
//        List<User> normalUsers = new ArrayList<>();
//        for (int i = 1; i <= 500; i++) {
//            User user = new User(
//                    "user" + i,
//                    "password",
//                    "USER",
//                    "User Address " + i,
//                    randomLatitude(),
//                    randomLongitude()
//            );
//            // 사용자 중요도와 선호 카테고리 설정
//            user.setImportance(randomImportance());
//            user.setPreferences(randomPreferences());
//            normalUsers.add(userRepository.save(user));
//        }
//
//        // 매장 및 메뉴 생성
//        List<Store> stores = new ArrayList<>();
//        int storeIdCounter = 1;
//        for (User owner : adminUsers) {
//            for (int j = 0; j < 3; j++) { // 각 어드민당 매장 3개 등록
//                String category = categories[random.nextInt(categories.length)];
//
//                Store store = new Store(
//                        category + " Store " + (storeIdCounter++),
//                        "Store Address " + storeIdCounter,
//                        "010-1234-56" + String.format("%02d", random.nextInt(100)),
//                        category,
//                        randomLatitude(),
//                        randomLongitude(),
//                        owner,
//                        20,
//                        random.nextInt(20) // 빈 테이블 수
//                );
//                store = storeRepository.save(store);
//                stores.add(store);
//
//                // 매장 분석 점수 생성
//                StoreAnalysisScore analysisScore = new StoreAnalysisScore();
//                analysisScore.setStoreId(store.getId());
//                analysisScore.setCleanliness(random.nextInt(101)); // 0~100 사이
//                analysisScore.setInterior(random.nextInt(101));    // 0~100 사이
//                analysisScore.setService(random.nextInt(101));     // 0~100 사이
//                analysisScore.setTaste(random.nextInt(101));       // 0~100 사이
//                analysisScore.setRatingscore(randomRatingScore()); // 1.0~5.0 사이 소수점 한자리
//                storeAnalysisScoreRepository.save(analysisScore);
//
//                // 메뉴 생성 (8개씩)
//                String[] possibleMenus = menuItemsMap.get(category);
//                List<String> selectedMenus = getRandomMenus(possibleMenus, 8);
//                for (String menuName : selectedMenus) {
//                    double price = 10000 + random.nextInt(41) * 1000; // 10000~50000 사이 1000원 단위
//                    Menu menu = new Menu(store, menuName, price);
//                    menuRepository.save(menu);
//
//                    // 일부 메뉴에 할인 등록
//                    if (random.nextBoolean()) {
//                        Discount discount = new Discount();
//                        discount.setMenu(menu);
//                        discount.setDiscountRate(randomDiscountRate());
//                        discount.setStartTime(LocalDateTime.now());
//                        discount.setEndTime(randomDiscountEndTime());
//                        discountRepository.save(discount);
//                    }
//                }
//
//                // 매장 혼잡도 생성
//                createStoreCongestionData(store);
//            }
//        }
//
//        // 리뷰 생성 (각 사용자가 모든 매장에 리뷰 2개씩 작성)
//        for (User user : normalUsers) {
//            for (Store store : stores) {
//                for (int i = 0; i < 2; i++) {
//                    String reviewContent = String.format(
//                            "맛: %s, 서비스: %s, 청결도: %s, 인테리어: %s",
//                            randomComment("훌륭하다", "좋다", "괜찮다", "별로다"),
//                            randomComment("친절하다", "보통이다", "불친절하다", "최악이다"),
//                            randomComment("깨끗하다", "보통이다", "더럽다", "청소 필요"),
//                            randomComment("아늑하다", "깔끔하다", "보통이다", "지저분하다")
//                    );
//                    Review review = new Review(
//                            reviewContent,
//                            random.nextInt(5) + 1, // 평점 (1~5)
//                            user,
//                            store
//                    );
//                    reviewRepository.save(review);
//                }
//            }
//        }
//
//        // 주문 생성 (매장별로)
//        for (Store store : stores) {
//            for (int i = 0; i < random.nextInt(10) + 1; i++) { // 매장당 1~10개의 주문 생성
//                User user = normalUsers.get(random.nextInt(normalUsers.size()));
//                Orders order = new Orders();
//                order.setUser(user);
//                order.setStore(store);
//                order.setOrderTime(LocalDateTime.now().minusDays(random.nextInt(30)));
//                order.setExpireTime(order.getOrderTime().plusHours(1));
//                ordersRepository.save(order);
//
//                // 주문 항목 추가
//                List<Menu> menus = store.getMenus();
//                int itemCount = random.nextInt(3) + 1; // 주문 항목 1~3개
//                for (int j = 0; j < itemCount; j++) {
//                    Menu menu = menus.get(random.nextInt(menus.size()));
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
//    private void createStoreCongestionData(Store store) {
//        LocalDate startDate = LocalDate.of(2024, 12, 1);
//        LocalDate endDate = LocalDate.of(2024, 12, 30);
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            int timeslot = random.nextInt(10) + 11; // 11~20
//            int congestionLevel = random.nextInt(5); // 0~4
//            StoreCongestion congestion = new StoreCongestion();
//            congestion.setStore(store);
//            congestion.setDate(date);
//            congestion.setTimeSlot(timeslot);
//            congestion.setCongestionLevel(congestionLevel);
//            storeCongestionRepository.save(congestion);
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
//        return Double.valueOf(String.format("%.6f", value));
//    }
//
//    private String randomComment(String... options) {
//        return options[random.nextInt(options.length)];
//    }
//
//    private String randomImportance() {
//        return importanceOptions[random.nextInt(importanceOptions.length)];
//    }
//
//    private String randomPreferences() {
//        int count = random.nextInt(categories.length) + 1; // 1~4개 선택
//        List<String> prefs = new ArrayList<>(Arrays.asList(categories));
//        Collections.shuffle(prefs);
//        return String.join(", ", prefs.subList(0, count));
//    }
//
//    private List<String> getRandomMenus(String[] possibleMenus, int count) {
//        List<String> menuList = new ArrayList<>(Arrays.asList(possibleMenus));
//        Collections.shuffle(menuList);
//        return menuList.subList(0, Math.min(count, menuList.size()));
//    }
//
//    private double randomDiscountRate() {
//        double[] rates = {0.1, 0.2, 0.3, 0.4, 0.5};
//        return rates[random.nextInt(rates.length)];
//    }
//
//    private LocalDateTime randomDiscountEndTime() {
//        int day = random.nextInt(7) + 24; // 24~30일 사이
//        return LocalDateTime.of(2024, 12, day, 23, 59);
//    }
//
//    private int randomRatingScore() {
//        return (int) (Math.round((random.nextDouble() * 4 + 1) * 10) / 10.0); // 1.0 ~ 5.0 소수점 한자리
//    }
//}