package com.demo.variety_store_mono.customer.controller.web;

import com.demo.variety_store_mono.customer.dto.response.ProductCatalogResponse;
import com.demo.variety_store_mono.customer.dto.summary.ProductCatalogSummary;
import com.demo.variety_store_mono.customer.service.ProductCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class ProductCatalogController {

    private final ProductCatalogService productCatalogService;

    /** 특정 카테고리에 포함되는 상품 목록 페이지 */
    @GetMapping("/categories/{categoryId}/products")
    public String productListPage(@PathVariable Long categoryId, Model model,
                                  @PageableDefault(page = 0, size = 5) Pageable pageable) {

        Page<ProductCatalogSummary> productCatalogList = productCatalogService.getProductCatalogList(categoryId, pageable);
        model.addAttribute("productCatalogList", productCatalogList);
        model.addAttribute("categoryId", categoryId);

        return "customer/content/product-list";
    }

    /** 상품 상세 페이지 */
    @GetMapping("/products/{productId}")
    public String productDetailsPage(@PathVariable Long productId, Model model) {

        ProductCatalogResponse productCatalogDetails = productCatalogService.getProductCatalogDetails(productId);
        model.addAttribute("product", productCatalogDetails);

        return "customer/content/product-detail";
    }
}
