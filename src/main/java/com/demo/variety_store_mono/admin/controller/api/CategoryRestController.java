package com.demo.variety_store_mono.admin.controller.api;

import com.demo.variety_store_mono.admin.response.GlobalOptionResponse;
import com.demo.variety_store_mono.admin.service.CategoryService;
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
}
