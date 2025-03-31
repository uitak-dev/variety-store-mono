package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.entity.QRole;
import com.demo.variety_store_mono.admin.dto.search.SearchRole;
import com.demo.variety_store_mono.admin.dto.response.RoleResponse;
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

public class CustomRoleRepositoryImpl implements CustomRoleRepository {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public CustomRoleRepositoryImpl(EntityManager em, ModelMapper modelMapper) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.modelMapper = modelMapper;
    }

    private QRole role = QRole.role;

    @Override
    public Page<RoleResponse> searchRoleList(SearchRole searchRole, Pageable pageable) {

        List<RoleResponse> content = queryFactory
                .selectFrom(role)
                .where(
                        roleNameEq(searchRole.getName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .toList();

        JPAQuery<Long> countQuery = queryFactory.select(role.count())
                .from(role)
                .where(
                        roleNameEq(searchRole.getName())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression roleNameEq(String roleName) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(roleName) ? role.name.containsIgnoreCase(roleName) : null;
    }
}
