package com.demo.variety_store_mono.customer.controller.api;

import com.demo.variety_store_mono.customer.dto.request.CartItemRequest;
import com.demo.variety_store_mono.customer.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api/carts")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;

    /** 장바구니에 상품 추가. */
    @PostMapping("/items")
    public ResponseEntity<String> addItemFromCart(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestBody CartItemRequest request) {
        if (jwt == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        Long userId = jwt.getClaim("id");
        cartService.addItemToCart(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /** 장바구니에 상품 제거. */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeItemFromCart(@AuthenticationPrincipal Jwt jwt,
                                                   @PathVariable Long cartItemId) {

        if (jwt == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        Long userId = jwt.getClaim("id");
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();  // 204 No Content 반환
    }

}
