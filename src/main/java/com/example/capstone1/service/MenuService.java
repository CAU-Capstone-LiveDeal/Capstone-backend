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
import java.util.Optional;
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
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if (!menu.getStore().getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to update this menu.");
        }

        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        Menu updatedMenu = menuRepository.save(menu);

        return toResponseDTO(updatedMenu);
    }

    public void deleteMenu(Long menuId, User currentUser) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if (!menu.getStore().getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to delete this menu.");
        }

        menuRepository.delete(menu);
    }

    public Optional<Menu> findById(Long menuId) {
        return menuRepository.findById(menuId);
    }

    public List<MenuResponseDTO> getMenusByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<Menu> menus = menuRepository.findByStore(store);
        return menus.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public MenuResponseDTO getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        return toResponseDTO(menu);
    }

    private MenuResponseDTO toResponseDTO(Menu menu) {
        MenuResponseDTO responseDTO = new MenuResponseDTO();
        responseDTO.setId(menu.getId());
        responseDTO.setName(menu.getName());
        responseDTO.setPrice(menu.getPrice());
        responseDTO.setStoreName(menu.getStore().getName());
        return responseDTO;
    }
}