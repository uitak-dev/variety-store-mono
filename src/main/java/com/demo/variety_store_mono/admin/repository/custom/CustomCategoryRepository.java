package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.dto.search.SearchCategory;
import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCategoryRepository {

    Page<CategorySummary> searchCategoryList(SearchCategory searchCategory, Pageable pageable);

    List<CategorySummary> findAllAncestors(Long categoryId);
}
