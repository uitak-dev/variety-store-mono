package com.demo.variety_store_mono.seller.repository.custom;

import com.demo.variety_store_mono.admin.dto.search.SearchProductManagement;
import com.demo.variety_store_mono.admin.dto.summary.ProductManagementSummary;
import com.demo.variety_store_mono.admin.entity.QCategory;
import com.demo.variety_store_mono.security.entity.QUser;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.*;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
    private QProductOption productOption = QProductOption.productOption;
    private QProductOptionValue productOptionValue = QProductOptionValue.productOptionValue;
    private QUser user = QUser.user;
    private QSeller seller = QSeller.seller;

    /**
     * [ 판매자 도메인: 검색 조건이 포함된 상품 목록 조회. ]
     * 옵션 상품(Product.single = false) 인 경우,
     * 총 재고량 = 해당 상품에 포함된 옵션 값의 재고량(ProductOptionValue.stockQuantity) 합.
     *
     * select
     *     p.*,
     *     case when p.single = true
     *          then p.stock_quantity
     *          else coalesce(sum(pov.stock_quantity), 0)
     *     end as stock_quantity
     * from
     *     product p
     * left join
     *     product_option po on po.product_id = p.product_id
     * left join
     *     product_option_value pov on pov.product_option_id = po.product_option_id
     * group by
     *     p.product_id;
     */
    @Override
    public Page<ProductSummary> searchProductList(Long sellerId, SearchProduct searchProduct, Pageable pageable) {

        NumberExpression<Integer> totalStockQuantity = Expressions.cases()
                .when(product.single.eq(true))
                .then(product.stockQuantity)
                .otherwise(productOptionValue.stockQuantity.sum().coalesce(0));

        List<ProductSummary> content = queryFactory
                .select(product, totalStockQuantity)
                .from(product)
                .where(
                        product.seller.id.eq(sellerId),
                        productNameEq(searchProduct.getName()),
                        productStatusEq(searchProduct.getStatus())
                )
                .leftJoin(product.productCategories, productCategory).fetchJoin()
                .leftJoin(productCategory.category, category).fetchJoin()
                .leftJoin(product.productOptions, productOption)
                .leftJoin(productOption.productOptionValues, productOptionValue)
                .groupBy(product.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    ProductSummary map = modelMapper.map(tuple.get(product), ProductSummary.class);
                    map.setStockQuantity(tuple.get(totalStockQuantity));
                    return map;
                })
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

    /** 관리자 도메인: 상품 목록 조회. */
    @Override
    public Page<ProductManagementSummary> searchProductManagementList(SearchProductManagement searchProduct, Pageable pageable) {

        List<ProductManagementSummary> content = queryFactory.selectFrom(product)
                .where(
                        productNameEq(searchProduct.getName()),
                        productStatusEq(searchProduct.getStatus()),
                        userNameEq(searchProduct.getUserName()),
                        categoryEq(searchProduct.getCategoryId())
                )
                .leftJoin(product.productCategories, productCategory).fetchJoin()
                .leftJoin(productCategory.category, category).fetchJoin()
                .leftJoin(product.seller, seller).fetchJoin()
                .leftJoin(product.seller.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(product -> {
                    ProductManagementSummary map = modelMapper.map(product, ProductManagementSummary.class);
                    map.setUserName(product.getSeller().getUser().getUserName());
                    return map;
                })
                .toList();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(
                        productNameEq(searchProduct.getName()),
                        productStatusEq(searchProduct.getStatus()),
                        userNameEq(searchProduct.getUserName()),
                        categoryEq(searchProduct.getCategoryId())
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

    private BooleanExpression userNameEq(String username) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(username) ? seller.user.userName.containsIgnoreCase(username) : null;
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? productCategory.category.id.eq(categoryId) : null;
    }
}
