package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.admin.dto.request.CategoryRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchCategory;
import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final GlobalOptionRepository globalOptionRepository;
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

        Set<GlobalOption> globalOptions = request.getOptionIds().stream().map(optionId ->
                globalOptionRepository.findById(optionId)
                        .orElseThrow(() -> new EntityNotFoundException("해당 옵션 템플릿을 찾을 수 없습니다.")))
                .collect(Collectors.toSet());

        Category category = Category.builder()
                .name(request.getName())
                .parent(parent)
                .build();

        category.updateGlobalOption(globalOptions);

        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    // 카테고리 목록 조회
    @Transactional(readOnly = true)
    public Page<CategorySummary> getCategorySearchList(SearchCategory searchCategory, Pageable pageable) {
        return categoryRepository.searchCategoryList(searchCategory, pageable);
    }

    // 최상위 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<CategorySummary> getTopCategories() {
        return categoryRepository.findTopCategories().stream()
                .map(category -> modelMapper.map(category, CategorySummary.class)).toList();
    }

    // 최하위 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<CategorySummary> getBottomCategories() {
        return categoryRepository.findBottomCategories().stream()
                .map(category -> modelMapper.map(category, CategorySummary.class)).toList();
    }

    // 특정 카테고리의 모든 하위 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategorySummary> getChildCategories(Long parentId) {
        return categoryRepository.findChildCategories(parentId).stream()
                .map(category -> modelMapper.map(category, CategorySummary.class)).toList();
    }

    // 특정 카테고리의 모든 상위 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategorySummary> getAllAncestors(Long categoryId) {
        return categoryRepository.findAllAncestors(categoryId);
    }

    // 카테고리 상세 조회
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {

        Category category = categoryRepository.findCategoryDetailsById(categoryId)
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

        Category category = categoryRepository.findCategoryDetailsById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId));

        // 카테고리 기본 정보 수정.
        category.update(request.getName(), parent);

        // 카테고리-옵션 연관관계 수정.
        Set<GlobalOption> collect = request.getOptionIds().stream()
                .map(optionId -> globalOptionRepository.findById(optionId)
                        .orElseThrow(() -> new EntityNotFoundException("해당 옵션 템플릿을 찾을 수 없습니다.")))
                .collect(Collectors.toSet());

        category.updateGlobalOption(collect);

        // 엔티티 -> dto 변환.
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    /** 카테고리 삭제 */
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    /** 카테고리에 포함되어 있는 옵션 템플릿 조회 */
    public List<GlobalOptionResponse> getGlobalOptionsInCategory(Long categoryId) {

        List<GlobalOptionResponse> ret = new ArrayList<>();

        Category category = categoryRepository.findCategoryDetailsById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. id: " + categoryId));

        return modelMapper.map(category, CategoryResponse.class).getGlobalOptions();
    }
}
