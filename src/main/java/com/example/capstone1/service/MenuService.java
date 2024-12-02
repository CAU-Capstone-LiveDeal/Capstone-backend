package com.example.capstone1.service;

import com.example.capstone1.dto.MenuRequestDTO;
import com.example.capstone1.dto.MenuResponseDTO;
import com.example.capstone1.model.Menu;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.MenuRepository;
import com.example.capstone1.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreRepository storeRepository;

    public MenuResponseDTO addMenu(Store store, MenuRequestDTO menuDTO) {
        Menu menu = new Menu(store, menuDTO.getName(), menuDTO.getPrice());
        Menu savedMenu = menuRepository.save(menu);
        return toResponseDTO(savedMenu);
    }

    public MenuResponseDTO updateMenu(Long menuId, MenuRequestDTO menuDTO, User currentUser) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        if (!menu.getStore().getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("이 메뉴를 수정할 권한이 없습니다.");
        }

        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        Menu updatedMenu = menuRepository.save(menu);

        return toResponseDTO(updatedMenu);
    }

    public void deleteMenu(Long menuId, User currentUser) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        if (!menu.getStore().getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("이 메뉴를 삭제할 권한이 없습니다.");
        }

        menuRepository.delete(menu);
    }

    public List<MenuResponseDTO> getMenusByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));

        List<Menu> menus = menuRepository.findByStore(store);
        return menus.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public MenuResponseDTO getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
        return toResponseDTO(menu);
    }

    private MenuResponseDTO toResponseDTO(Menu menu) {
        MenuResponseDTO responseDTO = new MenuResponseDTO();
        responseDTO.setId(menu.getId());
        responseDTO.setName(menu.getName());
        responseDTO.setPrice(menu.getPrice());
        responseDTO.setStoreName(menu.getStore().getName());
        responseDTO.setDiscountActive(menu.isDiscountActive());
        responseDTO.setDiscountRate(menu.getDiscountRate());

        // 할인된 가격을 100단위로 반올림하여 설정
        if (menu.isDiscountActive()) {
            double discountedPrice = Math.round(menu.getDiscountedPrice() / 100) * 100;
            responseDTO.setDiscountedPrice(discountedPrice);
        } else {
            responseDTO.setDiscountedPrice(menu.getDiscountedPrice());
        }

        return responseDTO;
    }
}