package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.admin.dto.response.ProductManagementResponse;
import com.demo.variety_store_mono.admin.dto.search.SearchProductManagement;
import com.demo.variety_store_mono.admin.dto.summary.ProductManagementSummary;
import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.admin.service.ProductManagementService;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductManagementController {

    private final ProductManagementService productManagementService;
    private final CategoryService categoryService;

    /** 상품 목록 페이지 */
    @GetMapping
    public String productList(@ModelAttribute SearchProductManagement searchProductManagement, Model model,
                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        model.addAttribute("productStatusList", ProductStatus.values());
        model.addAttribute("categoryList", categoryService.getBottomCategories());

        Page<ProductManagementSummary> productList = productManagementService.getProductSearchList(searchProductManagement, pageable);
        model.addAttribute("productList", productList);

        return "admin/content/product/product-list";
    }

    /** 상품 상세 조회 페이지 */
    @GetMapping("/{productId}")
    public String productDetails(@PathVariable Long productId, Model model) {

        ProductManagementResponse product = productManagementService.getProductDetails(productId);
        model.addAttribute("product", product);

        List<ProductStatus> filteredStatuses = Arrays.stream(ProductStatus.values())
                .filter(status -> status != ProductStatus.PENDING)
                .toList();
        model.addAttribute("productStatusList", filteredStatuses);

        return "admin/content/product/product-detail";
    }
}
