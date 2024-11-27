package com.example.capstone1.controller;

import com.example.capstone1.dto.DiscountRequestDTO;
import com.example.capstone1.dto.DiscountResponseDTO;
import com.example.capstone1.service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // 할인 등록
    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody DiscountRequestDTO discountRequestDTO) {
        try {
            DiscountResponseDTO responseDTO = discountService.createDiscount(discountRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("할인 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 할인 조회
    @GetMapping
    public ResponseEntity<List<DiscountResponseDTO>> getAllDiscounts() {
        List<DiscountResponseDTO> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    // 할인 삭제
    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long discountId) {
        try {
            discountService.deleteDiscount(discountId);
            return ResponseEntity.ok("할인이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("할인 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}