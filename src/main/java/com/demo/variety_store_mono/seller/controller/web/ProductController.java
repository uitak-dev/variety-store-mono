package com.demo.variety_store_mono.seller.controller.web;

import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import com.demo.variety_store_mono.seller.dto.request.ProductRequest;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import com.demo.variety_store_mono.seller.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /** 상품 등록 페이지 */
    @GetMapping("/new")
    public String productNew(Model model) {
        model.addAttribute("categories", categoryService.getBottomCategories());
        return "/seller/content/product/product-new";
    }

    /** 상품 등록 API */
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<ProductResponse> newProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody ProductRequest request) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(productService.createProduct(userId, request));
    }

    /** 상품 목록 페이지 */
    @GetMapping
    public String productList(@AuthenticationPrincipal Jwt jwt,
                              @ModelAttribute SearchProduct searchProduct,
                              Pageable pageable, Model model) {

        model.addAttribute("productStatusList", ProductStatus.values());

        Page<ProductSummary> productList = productService.getProductSearchList(jwt.getClaim("id"), searchProduct, pageable);
        model.addAttribute("productList", productList);

        return "seller/content/product/product-list";
    }

    /** 상품 상세 조회 페이지 */
    @GetMapping("/{productId}")
    public String productDetails(@AuthenticationPrincipal Jwt jwt,
                                 @PathVariable Long productId, Model model) {

        ProductResponse product = productService.getProductDetails(jwt.getClaim("id"), productId);
        model.addAttribute("product", product);

        return "seller/content/product/product-detail";
    }

    /** 상품 수정 페이지 */

    /** 상품 수정 API */

}
