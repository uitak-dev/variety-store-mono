package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.request.SearchCategory;
import com.demo.variety_store_mono.admin.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCategoryRepository {

    Page<CategoryResponse> searchCategoryList(SearchCategory searchCategory, Pageable pageable);

    List<CategoryResponse> findAllAncestors(Long categoryId);
}
