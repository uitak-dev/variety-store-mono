package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.QCategory;
import com.demo.variety_store_mono.admin.dto.search.SearchCategory;
import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public CustomCategoryRepositoryImpl(EntityManager em, ModelMapper modelMapper) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.modelMapper = modelMapper;
    }

    private QCategory category = QCategory.category;

    /** 검색 조건이 포함된 카테고리 목록 조회. */
    @Override
    public Page<CategorySummary> searchCategoryList(SearchCategory searchCategory, Pageable pageable) {

        List<CategorySummary> content = queryFactory
                .selectFrom(category)
                .where(
                        categoryNameEq(searchCategory.getName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(category -> modelMapper.map(category, CategorySummary.class))
                .toList();

        JPAQuery<Long> countQuery = queryFactory.select(category.count())
                .from(category)
                .where(
                        categoryNameEq(searchCategory.getName())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression categoryNameEq(String categoryName) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(categoryName) ? category.name.containsIgnoreCase(categoryName) : null;
    }

    /**
     * 주어진 카테고리의 모든 상위 카테고리 조회.
     * - 인덱스가 클수록 상위 카테고리
     */
    @Override
    public List<CategorySummary> findAllAncestors(Long categoryId) {

        List<Category> ret = new ArrayList<>();
        Category current = queryFactory.selectFrom(category)
                .where(category.id.eq(categoryId))
                .fetchOne();

        while (current != null && current.getParent() != null) {
            Long parentId = current.getParent().getId();
            current = queryFactory.selectFrom(category)
                    .where(category.id.eq(parentId))
                    .fetchOne();

            if (current != null) {
                ret.add(current);
            }
        }

        return ret.stream()
                .map(category -> modelMapper.map(category, CategorySummary.class))
                .toList();
    }

    /**
     * 주어진 카테고리를 포함한 모든 하위 카테고리 ID 조회.
     */
    @Override
    public List<Long> findAllDescendantCategoryIds(Long categoryId) {
        List<Long> resultCategoryIds = new ArrayList<>();
        resultCategoryIds.add(categoryId);

        Deque<Long> queue = new LinkedList<>();
        queue.addLast(categoryId);

        while (!queue.isEmpty()) {
            Long parentId = queue.removeFirst();
            List<Long> children = queryFactory.select(category.id)
                    .from(category)
                    .where(category.parent.id.eq(parentId))
                    .fetch();

            if (!children.isEmpty()) {
                children.forEach(queue::addLast);
                resultCategoryIds.addAll(children);
            }
        }

        return resultCategoryIds;
    }

}
