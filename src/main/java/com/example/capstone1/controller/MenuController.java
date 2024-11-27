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
import org.springframework.security.core.Authentication;
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

    // 메뉴 추가
    @PostMapping("/{storeId}")
    public ResponseEntity<?> addMenu(@PathVariable Long storeId, @RequestBody MenuRequestDTO menuDTO, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        Store store = storeService.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 매장에 메뉴를 추가할 권한이 없습니다.");
        }

        MenuResponseDTO createdMenu = menuService.addMenu(store, menuDTO);
        return ResponseEntity.ok(createdMenu);
    }

    // 메뉴 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequestDTO menuDTO, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        MenuResponseDTO updatedMenu = menuService.updateMenu(menuId, menuDTO, currentUser);
        return ResponseEntity.ok(updatedMenu);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        menuService.deleteMenu(menuId, currentUser);
        return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
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