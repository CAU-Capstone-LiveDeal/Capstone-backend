package com.example.capstone1.repository;

import com.example.capstone1.model.StoreCongestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StoreCongestionRepository extends JpaRepository<StoreCongestion, Long> {

    List<StoreCongestion> findByStoreIdAndDate(Long storeId, LocalDate date);
}