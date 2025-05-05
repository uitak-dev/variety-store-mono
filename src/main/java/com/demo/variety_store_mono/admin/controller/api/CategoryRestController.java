package com.demo.variety_store_mono.admin.controller.api;

import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.admin.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    /** 특정 카테고리에 등록되어 있는 옵션 템플릿 조회 */
    @GetMapping("/{categoryId}/options")
    public ResponseEntity<List<GlobalOptionResponse>> getGlobalOptions(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getGlobalOptionsInCategory(categoryId));
    }

    /** 특정 카테고리의 하위 카테고리 목록 조회 */
    @GetMapping("/children")
    public ResponseEntity<List<CategorySummary>> subCategories(@RequestParam(required = false) Long categoryId) {

        if (categoryId == null || categoryId == 0L) {
            return ResponseEntity.ok(categoryService.getTopCategories());
        }
        return ResponseEntity.ok(categoryService.getChildCategories(categoryId));
    }

    /** 모든 카테고리 조회 */
    @GetMapping
    public ResponseEntity<List<CategoryService.CategoryTreeDto>> findAllCategories() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }
}
