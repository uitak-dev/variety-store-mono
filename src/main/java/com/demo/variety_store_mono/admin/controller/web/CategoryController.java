package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.admin.dto.request.CategoryRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchCategory;
import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.admin.service.CategoryService;
import com.demo.variety_store_mono.admin.service.GlobalOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final GlobalOptionService globalOptionService;

    /** 카테고리 목록 조회 페이지 */
    @GetMapping
    public String categoryList(@ModelAttribute SearchCategory searchCategory, Model model,
                               Pageable pageable) {

        Page<CategorySummary> categoryList = categoryService.getCategorySearchList(searchCategory, pageable);
        model.addAttribute("categoryList", categoryList);

        return "admin/content/category/category-list";
    }

    /** 카테고리 상세 조회 페이지 */
    @GetMapping("/{categoryId}")
    public String categoryDetails(@PathVariable Long categoryId, Model model) {

        CategoryResponse category = categoryService.getCategory(categoryId);
        model.addAttribute("category", category);

        // 상위 카테고리 목록.( 인덱스가 클수록 상위 카테고리 )
        List<CategorySummary> parentChain = categoryService.getAllAncestors(categoryId);
        if (!parentChain.isEmpty()) {
            StringBuilder parentChainPath = new StringBuilder();
            for (int i = parentChain.size() - 1; i >= 0; i--) {
                parentChainPath.append(parentChain.get(i).getName());
                if (i != 0) parentChainPath.append(" > ");
            }
            model.addAttribute("parentChainPath", parentChainPath.toString());
        }

        return "admin/content/category/category-detail";
    }

    /** 카테고리 등록 페이지 */
    @GetMapping("/new")
    public String categoryNew(Model model) {

        model.addAttribute("options", globalOptionService.getAllOption());
        model.addAttribute("category", new CategoryRequest());

        return "admin/content/category/category-new";
    }

    /** 카테고리 등록 API */
    @PostMapping("/new")
    public String newCategory(@ModelAttribute("category") @Valid CategoryRequest request, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            model.addAttribute("options", globalOptionService.getAllOption());
            return "admin/content/category/category-new";
        }

        CategoryResponse category = categoryService.createCategory(request);
        redirectAttributes.addAttribute("categoryId", category.getId());

        return "redirect:/admin/categories/{categoryId}";
    }

    /** 카테고리 수정 페이지 */
    @GetMapping("/{categoryId}/edit")
    public String categoryUpdate(@PathVariable Long categoryId, Model model) {

        // 현재 카테고리 조회.
        CategoryResponse category = categoryService.getCategory(categoryId);

        // 상위 카테고리 목록.( 인덱스가 클수록 상위 카테고리 )
        List<CategorySummary> parentChain = categoryService.getAllAncestors(categoryId);
        if (!parentChain.isEmpty()) {
            StringBuilder parentChainPath = new StringBuilder();
            for (int i = parentChain.size() - 1; i >= 0; i--) {
                parentChainPath.append(parentChain.get(i).getName());
                if (i != 0) parentChainPath.append(" > ");
            }
            model.addAttribute("parentChainPath", parentChainPath.toString());
        }

        model.addAttribute("category", category);
        model.addAttribute("options", globalOptionService.getAllOption());

        return "admin/content/category/category-edit";
    }

    /** 카테고리 수정 API */
    @PostMapping("/{categoryId}/edit")
    public String updateCategory(@PathVariable Long categoryId, CategoryRequest request) {
        CategoryResponse category = categoryService.updateCategory(categoryId, request);
        return "redirect:/admin/categories/{categoryId}";
    }
}
