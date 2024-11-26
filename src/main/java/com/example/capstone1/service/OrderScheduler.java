//package com.example.capstone1.service;
//
//import com.example.capstone1.model.Orders;
//import com.example.capstone1.model.Store;
//import com.example.capstone1.repository.OrdersRepository;
//import com.example.capstone1.repository.StoreRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class OrderScheduler {
//
//    @Autowired
//    private OrdersRepository ordersRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    /**
//     * 매 분마다 만료된 주문 처리
//     */
//    @Scheduled(fixedRate = 60000) // 60초마다 실행
//    public void processExpiredOrders() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // JPQL 기반 메서드로 만료된 주문 조회
//        List<Orders> expiredOrders = ordersRepository.findExpiredOrders(now);
//
//        for (Orders order : expiredOrders) {
//            Store store = order.getStore();
//
//            // 빈 테이블 개수 복구
//            store.increaseEmptyTables();
//            storeRepository.save(store);
//
//            // 주문을 만료 상태로 변경
//            order.setExpired(true);
//            ordersRepository.save(order);
//        }
//    }
//}