package com.demo.variety_store_mono.seller.controller.web;

import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import com.demo.variety_store_mono.seller.dto.request.ProductRequest;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import com.demo.variety_store_mono.seller.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "seller/content/product/product-new";
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
                              @ModelAttribute SearchProduct searchProduct, Model model,
                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

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
    @GetMapping("/{productId}/edit")
    public String productUpdate(@AuthenticationPrincipal Jwt jwt, RedirectAttributes redirectAttributes,
                                @PathVariable Long productId, Model model) {

        ProductResponse product = productService.getProductDetails(jwt.getClaim("id"), productId);

        // 상품 상태가 '등록 대기' 라면, 수정 불가.( 상품 상세 페이지로 리다이렉트 )
        if (product.getStatus() == ProductStatus.PENDING) {
            redirectAttributes.addFlashAttribute("errorMessage", "등록 대기 상태인 상품은 수정할 수 없습니다.");
            return "redirect:/seller/products/{productId}";
        }

        model.addAttribute("product", product);
        return "seller/content/product/product-edit";
    }

    /** 상품 수정 API */
    @PostMapping("/{productId}/edit")
    @ResponseBody
    public ResponseEntity<ProductResponse> updateProduct(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId,
                                @RequestBody ProductRequest request, Model model) {

        return ResponseEntity.ok(productService.updateProduct(productId, jwt.getClaim("id"), request));
    }
}
