package com.example.capstone1.controller;

import com.example.capstone1.model.Discount;
import com.example.capstone1.model.Menu;
import com.example.capstone1.model.User;
import com.example.capstone1.service.DiscountService;
import com.example.capstone1.service.MenuService;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @PostMapping("/{menuId}")
    public ResponseEntity<?> addDiscount(@PathVariable Long menuId, @RequestBody Discount discount) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        Menu menu = menuService.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if (!menu.getStore().getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add discounts for this menu.");
        }

        discount.setMenu(menu);
        return ResponseEntity.ok(discountService.saveDiscount(discount));
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long discountId, @RequestBody Discount updatedDiscount) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        Discount discount = discountService.findById(discountId).orElseThrow(() -> new IllegalArgumentException("Discount not found"));

        if (!discount.getMenu().getStore().getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this discount.");
        }

        discount.setPercentage(updatedDiscount.getPercentage());
        discount.setStartTime(updatedDiscount.getStartTime());
        discount.setEndTime(updatedDiscount.getEndTime());

        return ResponseEntity.ok(discountService.saveDiscount(discount));
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long discountId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        Discount discount = discountService.findById(discountId).orElseThrow(() -> new IllegalArgumentException("Discount not found"));

        if (!discount.getMenu().getStore().getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this discount.");
        }

        discountService.deleteDiscount(discount);
        return ResponseEntity.ok("Discount deleted successfully.");
    }
}