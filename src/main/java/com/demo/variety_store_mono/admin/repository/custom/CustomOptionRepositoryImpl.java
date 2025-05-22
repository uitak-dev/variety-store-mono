package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.dto.summary.GlobalOptionSummary;
import com.demo.variety_store_mono.admin.entity.QGlobalOption;
import com.demo.variety_store_mono.admin.dto.search.SearchOption;
import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
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


public class CustomOptionRepositoryImpl implements CustomOptionRepository {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public CustomOptionRepositoryImpl(EntityManager em, ModelMapper modelMapper) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.modelMapper = modelMapper;
    }

    private QGlobalOption globalOption = QGlobalOption.globalOption;

    @Override
    public Page<GlobalOptionSummary> searchOptionList(SearchOption searchOption, Pageable pageable) {

        List<GlobalOptionSummary> content = queryFactory
                .selectFrom(globalOption)
                .where(
                        optionNameEq(searchOption.getName())
                )
                .orderBy(globalOption.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(option -> modelMapper.map(option, GlobalOptionSummary.class))
                .toList();

        JPAQuery<Long> countQuery = queryFactory.select(globalOption.count())
                .from(globalOption)
                .where(
                        optionNameEq(searchOption.getName())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression optionNameEq(String optionName) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(optionName) ? globalOption.name.containsIgnoreCase(optionName) : null;
    }
}
