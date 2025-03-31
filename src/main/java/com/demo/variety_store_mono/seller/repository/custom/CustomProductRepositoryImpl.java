package com.demo.variety_store_mono.seller.repository.custom;

import com.demo.variety_store_mono.admin.entity.QCategory;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import com.demo.variety_store_mono.seller.entity.QProduct;
import com.demo.variety_store_mono.seller.entity.QProductCategory;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
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

import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public CustomProductRepositoryImpl(EntityManager em, ModelMapper modelMapper) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.modelMapper = modelMapper;
    }

    private QProduct product = QProduct.product;
    private QProductCategory productCategory = QProductCategory.productCategory;
    private QCategory category = QCategory.category;

    /** 검색 조건이 포함된 상품 목록 조회. */
    @Override
    public Page<ProductListResponse> searchProductList(Long sellerId, SearchProduct searchProduct, Pageable pageable) {

        List<ProductListResponse> content = queryFactory.selectFrom(product)
                .where(
                        product.seller.id.eq(sellerId),
                        productNameEq(searchProduct.getName()),
                        productStatusEq(searchProduct.getStatus())
                )
                .leftJoin(product.productCategories, productCategory).fetchJoin()
                .leftJoin(productCategory.category, category).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(product -> modelMapper.map(product, ProductListResponse.class))
                .toList();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(
                        product.seller.id.eq(sellerId),
                        productNameEq(searchProduct.getName()),
                        productStatusEq(searchProduct.getStatus())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression productNameEq(String productName) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(productName) ? product.name.containsIgnoreCase(productName) : null;
    }

    private BooleanExpression productStatusEq(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }
}
