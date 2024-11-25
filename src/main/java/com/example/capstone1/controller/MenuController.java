package com.example.capstone1.controller;

import com.example.capstone1.dto.MenuRequestDTO;
import com.example.capstone1.dto.MenuResponseDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.service.MenuService;
import com.example.capstone1.service.StoreService;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @PostMapping("/{storeId}")
    public ResponseEntity<?> addMenu(@PathVariable Long storeId, @RequestBody MenuRequestDTO menuDTO) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        Store store = storeService.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found"));

        if (!store.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add menus for this store.");
        }

        MenuResponseDTO createdMenu = menuService.addMenu(store, menuDTO);
        return ResponseEntity.ok(createdMenu);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequestDTO menuDTO) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        MenuResponseDTO updatedMenu = menuService.updateMenu(menuId, menuDTO, currentUser);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username);

        menuService.deleteMenu(menuId, currentUser);
        return ResponseEntity.ok("Menu deleted successfully.");
    }

    // 특정 매장의 메뉴 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<MenuResponseDTO>> getMenusByStore(@PathVariable Long storeId) {
        List<MenuResponseDTO> menus = menuService.getMenusByStore(storeId);
        return ResponseEntity.ok(menus);
    }

    // 특정 메뉴 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long menuId) {
        MenuResponseDTO menu = menuService.getMenuById(menuId);
        return ResponseEntity.ok(menu);
    }
}