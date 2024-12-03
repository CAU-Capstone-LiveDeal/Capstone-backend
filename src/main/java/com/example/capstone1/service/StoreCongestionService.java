package com.example.capstone1.service;

import com.example.capstone1.dto.StoreCongestionRequestDTO;
import com.example.capstone1.dto.StoreCongestionResponseDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.StoreCongestion;
import com.example.capstone1.repository.StoreCongestionRepository;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class StoreCongestionService {

    private final StoreRepository storeRepository;
    private final StoreCongestionRepository storeCongestionRepository;

    public StoreCongestionService(StoreRepository storeRepository, StoreCongestionRepository storeCongestionRepository) {
        this.storeRepository = storeRepository;
        this.storeCongestionRepository = storeCongestionRepository;
    }

    public StoreCongestionResponseDTO registerCongestion(StoreCongestionRequestDTO request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        StoreCongestion congestion = new StoreCongestion();
        congestion.setStore(store);
        congestion.setTimeSlot(request.getTimeSlot());
        congestion.setCongestionLevel(request.getCongestionLevel());
        congestion.setDate(request.getDate());

        StoreCongestion savedCongestion = storeCongestionRepository.save(congestion);

        // 저장된 데이터로 간단한 응답 객체 생성
        return new StoreCongestionResponseDTO(
                savedCongestion.getDate(),
                savedCongestion.getTimeSlot(),
                savedCongestion.getCongestionLevel()
        );
    }

    public List<StoreCongestionResponseDTO> getCongestionByStoreAndDate(Long storeId, LocalDate date) {
        List<StoreCongestion> congestionList = storeCongestionRepository.findByStoreIdAndDate(storeId, date);
        List<StoreCongestionResponseDTO> responseList = new ArrayList<>();

        for (int hour = 11; hour <= 20; hour++) {
            int finalHour = hour;
            StoreCongestion congestion = congestionList.stream()
                    .filter(c -> c.getTimeSlot() == finalHour)
                    .findFirst()
                    .orElse(null);

            responseList.add(new StoreCongestionResponseDTO(
                    date,
                    hour,
                    (congestion != null) ? congestion.getCongestionLevel() : 0
            ));
        }

        return responseList;
    }
}