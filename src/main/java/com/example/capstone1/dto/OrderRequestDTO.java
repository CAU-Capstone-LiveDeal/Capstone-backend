package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {

    // 주문할 메뉴 목록
    private List<MenuOrderDTO> menuOrders;

    @Getter
    @Setter
    public static class MenuOrderDTO {
        private Long menuId;
        private Integer quantity;
    }
}