package com.example.capstone1.service;

import com.example.capstone1.dto.DiscountRequestDTO;
import com.example.capstone1.dto.DiscountResponseDTO;
import com.example.capstone1.model.Discount;
import com.example.capstone1.model.Menu;
import com.example.capstone1.model.Store;
import com.example.capstone1.repository.DiscountRepository;
import com.example.capstone1.repository.MenuRepository;
import com.example.capstone1.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public DiscountService(DiscountRepository discountRepository, MenuRepository menuRepository, StoreRepository storeRepository) {
        this.discountRepository = discountRepository;
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
    }

    // 할인 등록
    @Transactional
    public DiscountResponseDTO createDiscount(DiscountRequestDTO discountRequestDTO) {
        Menu menu = menuRepository.findById(discountRequestDTO.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        Discount discount = new Discount();
        discount.setMenu(menu);
        discount.setDiscountRate(discountRequestDTO.getDiscountRate());
        discount.setStartTime(discountRequestDTO.getStartTime());
        discount.setEndTime(discountRequestDTO.getStartTime().plusHours(discountRequestDTO.getDurationHours()));
        discount.setActive(true); // 할인 등록 시 자동 활성화

        // 메뉴의 할인 상태 업데이트
        menu.applyDiscount(discount.getDiscountRate());
        menuRepository.save(menu);

        // 매장의 할인 상태 업데이트
        Store store = menu.getStore();
        store.updateDiscountStatus();
        storeRepository.save(store);

        Discount savedDiscount = discountRepository.save(discount);

        return convertToResponseDTO(savedDiscount);
    }

    // 할인 비활성화 (할인 종료 시간에 맞춰)
    @Transactional
    public void deactivateExpiredDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> discounts = discountRepository.findByEndTimeBeforeAndActiveTrue(now);

        for (Discount discount : discounts) {
            discount.setActive(false);
            discountRepository.save(discount);

            // 메뉴의 할인 상태 업데이트
            Menu menu = discount.getMenu();
            menu.removeDiscount();
            menuRepository.save(menu);

            // 매장의 할인 상태 업데이트
            Store store = menu.getStore();
            store.updateDiscountStatus();
            storeRepository.save(store);
        }
    }

    // 할인 조회
    public List<DiscountResponseDTO> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    // 할인 삭제
    @Transactional
    public void deleteDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("할인을 찾을 수 없습니다."));

        // 할인 삭제 시 메뉴와 매장의 할인 상태 업데이트
        if (discount.isActive()) {
            Menu menu = discount.getMenu();
            menu.removeDiscount();
            menuRepository.save(menu);

            Store store = menu.getStore();
            store.updateDiscountStatus();
            storeRepository.save(store);
        }

        discountRepository.delete(discount);
    }

    // 할인 활성화 (스케줄러에서 호출)
    @Transactional
    public void activateDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> discounts = discountRepository.findByStartTime(now);

        for (Discount discount : discounts) {
            if (!discount.isActive()) {
                discount.setActive(true);
                discountRepository.save(discount);

                // 메뉴의 할인 상태 업데이트
                Menu menu = discount.getMenu();
                menu.applyDiscount(discount.getDiscountRate());
                menuRepository.save(menu);

                // 매장의 할인 상태 업데이트
                Store store = menu.getStore();
                store.updateDiscountStatus();
                storeRepository.save(store);
            }
        }
    }

    // Discount 엔티티를 DiscountResponseDTO로 변환
    private DiscountResponseDTO convertToResponseDTO(Discount discount) {
        DiscountResponseDTO dto = new DiscountResponseDTO();
        dto.setDiscountId(discount.getId());
        dto.setMenuId(discount.getMenu().getId());
        dto.setMenuName(discount.getMenu().getName());
        dto.setDiscountRate(discount.getDiscountRate());
        dto.setStartTime(discount.getStartTime());
        dto.setEndTime(discount.getEndTime());
        dto.setActive(discount.isActive());
        return dto;
    }
}