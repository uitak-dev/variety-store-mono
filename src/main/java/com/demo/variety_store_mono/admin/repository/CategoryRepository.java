package com.demo.variety_store_mono.admin.repository;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.repository.custom.CustomCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {

    Optional<Category> findByName(String name);

    /** 최상위 카테고리 목록 조회. */
    @Query("select c from Category c where c.parent is null")
    List<Category> findTopCategories();

    /** 특정 카테고리의 하위 카테고리 목록 조회. */
    @Query("select c from Category c where c.parent.id = :parentId")
    List<Category> findChildCategories(@Param("parentId") Long parentId);

    /** 카테고리 상세 정보 조회 */
    @Query("select distinct c from Category c " +
            "left join fetch c.categoryGlobalOptions cgo " +
            "left join fetch cgo.globalOption " +
            "where c.id = :categoryId")
    Optional<Category> findCategoryByIdWithOption(@Param("categoryId") Long categoryId);
}
