package com.demo.variety_store_mono.common.repository.custom;


import com.demo.variety_store_mono.common.entity.QUser;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.request.UserSearch;
import com.demo.variety_store_mono.common.response.SimpleUserInfo;
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

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public CustomUserRepositoryImpl(EntityManager em, ModelMapper modelMapper) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.modelMapper = modelMapper;
    }

    private QUser user = QUser.user;

    @Override
    public Page<SimpleUserInfo> searchUserListByUserType(UserType userType, UserSearch userSearch, Pageable pageable) {

        List<SimpleUserInfo> content = queryFactory
                .selectFrom(user)
                .where(
                        user.userType.eq(userType),
                        usernameEq(userSearch.getUserName()),
                        emailEq(userSearch.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(user -> modelMapper.map(user, SimpleUserInfo.class))
                .toList();

        // 전체 개수 조회( 페이징 처리 )
        JPAQuery<Long> countQuery = queryFactory
                .select(user.count())
                .from(user)
                .where(
                        user.userType.eq(userType),
                        usernameEq(userSearch.getUserName()),
                        emailEq(userSearch.getEmail())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameEq(String userName) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(userName) ? user.userName.containsIgnoreCase(userName) : null;
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasLength(email) ? user.email.containsIgnoreCase(email) : null;
    }
}
