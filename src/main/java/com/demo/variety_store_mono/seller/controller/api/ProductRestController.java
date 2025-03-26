package com.demo.variety_store_mono.seller.controller.api;

import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.admin.service.GlobalOptionService;
import com.demo.variety_store_mono.seller.request.ProductRequest;
import com.demo.variety_store_mono.seller.response.ProductResponse;
import com.demo.variety_store_mono.seller.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final GlobalOptionService globalOptionService;

    /** 상품 등록 API */
    @PostMapping("/new")
    public ResponseEntity<ProductResponse> newProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody ProductRequest request) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(productService.createProduct(userId, request));
    }
}
