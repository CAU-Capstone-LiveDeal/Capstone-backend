package com.example.capstone1.service;

import com.example.capstone1.dto.OrderRequestDTO;
import com.example.capstone1.dto.OrderResponseDTO;
import com.example.capstone1.model.*;
import com.example.capstone1.repository.MenuRepository;
import com.example.capstone1.repository.OrdersRepository;
import com.example.capstone1.repository.StoreRepository;
import com.example.capstone1.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public OrdersService(OrdersRepository ordersRepository, MenuRepository menuRepository,
                         StoreRepository storeRepository, UserRepository userRepository) {
        this.ordersRepository = ordersRepository;
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    // 주문 생성
    @Transactional
    public Orders createOrder(OrderRequestDTO orderRequestDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (orderRequestDTO.getMenuOrders() == null || orderRequestDTO.getMenuOrders().isEmpty()) {
            throw new IllegalArgumentException("주문할 메뉴가 없습니다.");
        }

        // 첫 번째 메뉴로부터 매장 추론
        Menu firstMenu = menuRepository.findById(orderRequestDTO.getMenuOrders().get(0).getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
        Store store = firstMenu.getStore();

        // 매장의 빈 테이블 수 확인
        if (store.getEmptyTables() <= 0) {
            throw new IllegalStateException("빈 테이블이 없습니다.");
        }

        // 주문 생성
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setStore(store);
        orders.setOrderTime(LocalDateTime.now());
        orders.setExpireTime(LocalDateTime.now().plusMinutes(2)); // 2분 후 만료
        orders.setExpired(false);

        // 주문 항목 추가
        for (OrderRequestDTO.MenuOrderDTO menuOrderDTO : orderRequestDTO.getMenuOrders()) {
            Menu menu = menuRepository.findById(menuOrderDTO.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            // 모든 메뉴가 동일한 매장에 속하는지 확인
            if (!menu.getStore().equals(store)) {
                throw new IllegalArgumentException("모든 메뉴는 동일한 매장의 메뉴여야 합니다.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenu(menu);
            orderItem.setQuantity(menuOrderDTO.getQuantity());
            orders.addOrderItem(orderItem);
        }

        // 매장의 빈 테이블 수 감소
        store.setEmptyTables(store.getEmptyTables() - 1);
        storeRepository.save(store);

        return ordersRepository.save(orders);
    }

    // 사용자별 주문 조회
    public List<OrderResponseDTO> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Orders> ordersList = ordersRepository.findByUser(user);

        return ordersList.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    // 주문 ID로 주문 조회
    public OrderResponseDTO getOrderById(Long orderId) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        return convertToResponseDTO(orders);
    }

    // 매장별 주문 조회
    public List<OrderResponseDTO> getOrdersByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));

        List<Orders> ordersList = ordersRepository.findByStore(store);

        return ordersList.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    // 주문 수정
    @Transactional
    public void updateOrder(Long orderId, OrderRequestDTO orderRequestDTO, String username) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 주문한 사용자와 현재 사용자가 동일한지 확인
        if (!orders.getUser().getUsername().equals(username)) {
            throw new IllegalAccessError("수정 권한이 없습니다.");
        }

        // 기존 주문 항목 삭제
        orders.getOrderItems().clear();

        // 새로운 주문 항목 추가
        for (OrderRequestDTO.MenuOrderDTO menuOrderDTO : orderRequestDTO.getMenuOrders()) {
            Menu menu = menuRepository.findById(menuOrderDTO.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenu(menu);
            orderItem.setQuantity(menuOrderDTO.getQuantity());
            orders.addOrderItem(orderItem);
        }

        ordersRepository.save(orders);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId, String username) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 주문한 사용자와 현재 사용자가 동일한지 확인
        if (!orders.getUser().getUsername().equals(username)) {
            throw new IllegalAccessError("삭제 권한이 없습니다.");
        }

        // 매장의 빈 테이블 수 증가
        Store store = orders.getStore();
        store.setEmptyTables(Math.min(store.getEmptyTables() + 1, store.getTotalTables()));
        storeRepository.save(store);

        ordersRepository.delete(orders);
    }

    // 주문 만료 처리
    @Transactional
    public void expireOrders() {
        List<Orders> expiredOrders = ordersRepository.findExpiredOrders(LocalDateTime.now());

        for (Orders orders : expiredOrders) {
            if (!orders.isExpired()) {
                orders.setExpired(true);

                // 매장의 빈 테이블 수 증가
                Store store = orders.getStore();
                store.setEmptyTables(Math.min(store.getEmptyTables() + 1, store.getTotalTables()));
                storeRepository.save(store);

                ordersRepository.save(orders);
            }
        }
    }

    // Orders 엔티티를 OrderResponseDTO로 변환
    private OrderResponseDTO convertToResponseDTO(Orders orders) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(orders.getId());
        dto.setStoreId(orders.getStore().getId());
        dto.setOrderTime(orders.getOrderTime());
        dto.setExpireTime(orders.getExpireTime());
        dto.setExpired(orders.isExpired());

        List<OrderResponseDTO.MenuOrderDTO> menuOrders = orders.getOrderItems().stream().map(item -> {
            OrderResponseDTO.MenuOrderDTO menuOrderDTO = new OrderResponseDTO.MenuOrderDTO();
            menuOrderDTO.setMenuId(item.getMenu().getId());
            menuOrderDTO.setMenuName(item.getMenu().getName());
            menuOrderDTO.setQuantity(item.getQuantity());
            return menuOrderDTO;
        }).collect(Collectors.toList());

        dto.setMenuOrders(menuOrders);

        return dto;
    }
}