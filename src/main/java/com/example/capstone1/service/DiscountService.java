package com.example.capstone1.service;

import com.example.capstone1.model.Discount;
import com.example.capstone1.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Optional<Discount> findById(Long discountId) {
        return discountRepository.findById(discountId);
    }

    public void deleteDiscount(Discount discount) {
        discountRepository.delete(discount);
    }
}