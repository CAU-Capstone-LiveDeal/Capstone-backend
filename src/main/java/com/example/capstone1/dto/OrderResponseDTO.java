package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long orderId;
    private Long storeId;
    private LocalDateTime orderTime;
    private LocalDateTime expireTime;
    private boolean expired;
    private List<MenuOrderDTO> menuOrders;

    @Getter
    @Setter
    public static class MenuOrderDTO {
        private Long menuId;
        private String menuName;
        private Integer quantity;
    }
}