package com.demo.variety_store_mono.seller.controller.web;

import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.admin.service.GlobalOptionService;
import com.demo.variety_store_mono.seller.request.ProductRequest;
import com.demo.variety_store_mono.seller.response.ProductResponse;
import com.demo.variety_store_mono.seller.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final GlobalOptionService globalOptionService;

    /** 상품 등록 페이지 */
    @GetMapping("/new")
    public String productNew(Model model) {
        model.addAttribute("categories", categoryService.getTopCategories());
        return "/seller/content/product/product-new";
    }

    /** 상품 등록 API */
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<ProductResponse> newProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody ProductRequest request) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(productService.createProduct(userId, request));
    }

}
