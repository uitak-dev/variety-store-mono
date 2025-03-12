package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.request.CategoryRequest;
import com.demo.variety_store_mono.admin.request.SearchCategory;
import com.demo.variety_store_mono.admin.response.CategoryResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /** 카테고리 생성 */
    public CategoryResponse createCategory(CategoryRequest request) {

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("상위 카테고리를 찾을 수 없습니다. id: " + request.getParentId()));
        }

        Optional<Category> findCategory = categoryRepository.findByName(request.getName());
        if (findCategory.isPresent()) {
            throw new RuntimeException("중복된 카테고리 이름입니다.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .parent(parent)
                .build();

        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    // 카테고리 목록 조회
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategorySearchList(SearchCategory searchCategory, Pageable pageable) {
        return categoryRepository.searchCategoryList(searchCategory, pageable);
    }

    // 최상위 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<CategoryResponse> getTopCategories() {

        return categoryRepository.findTopCategories().stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
    }

    // 특정 카테고리의 모든 하위 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategoryResponse> getChildCategories(Long parentId) {
        return categoryRepository.findChildCategories(parentId).stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class)).toList();
    }

    // 특정 카테고리의 모든 상위 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllAncestors(Long categoryId) {
        return categoryRepository.findAllAncestors(categoryId);
    }

    // 카테고리 상세 조회
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId));

        return modelMapper.map(category, CategoryResponse.class);
    }

    /** 카테고리 수정 */
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("상위 카테고리를 찾을 수 없습니다. id: " + request.getParentId()));
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId));

        category.update(request.getName(), parent);
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    /** 카테고리 삭제 */
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}
