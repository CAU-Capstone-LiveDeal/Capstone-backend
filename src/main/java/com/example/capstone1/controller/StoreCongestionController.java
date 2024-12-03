package com.example.capstone1.controller;

import com.example.capstone1.dto.StoreCongestionRequestDTO;
import com.example.capstone1.dto.StoreCongestionResponseDTO;
import com.example.capstone1.model.StoreCongestion;
import com.example.capstone1.service.StoreCongestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/store-congestion")
public class StoreCongestionController {

    private final StoreCongestionService storeCongestionService;

    public StoreCongestionController(StoreCongestionService storeCongestionService) {
        this.storeCongestionService = storeCongestionService;
    }

    @PostMapping("/register")
    public ResponseEntity<StoreCongestionResponseDTO> registerCongestion(@RequestBody StoreCongestionRequestDTO request) {
        StoreCongestionResponseDTO response = storeCongestionService.registerCongestion(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}/all")
    public ResponseEntity<List<StoreCongestionResponseDTO>> getAllCongestions(
            @PathVariable Long storeId,
            @RequestParam(required = false) String date) {
        LocalDate queryDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        List<StoreCongestionResponseDTO> responseList = storeCongestionService.getCongestionByStoreAndDate(storeId, queryDate);
        return ResponseEntity.ok(responseList);
    }
}