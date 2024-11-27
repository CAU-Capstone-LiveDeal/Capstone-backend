package com.example.capstone1.service;

import com.example.capstone1.dto.DiscountRequestDTO;
import com.example.capstone1.dto.DiscountResponseDTO;
import com.example.capstone1.model.Discount;
import com.example.capstone1.model.Menu;
import com.example.capstone1.repository.DiscountRepository;
import com.example.capstone1.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final MenuRepository menuRepository;

    public DiscountService(DiscountRepository discountRepository, MenuRepository menuRepository) {
        this.discountRepository = discountRepository;
        this.menuRepository = menuRepository;
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