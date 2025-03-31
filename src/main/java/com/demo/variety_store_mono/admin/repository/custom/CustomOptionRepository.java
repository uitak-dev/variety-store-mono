package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.dto.search.SearchOption;
import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
import com.demo.variety_store_mono.admin.dto.summary.GlobalOptionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOptionRepository {

    /** 글로벌 옵션 목록 조회 */
    Page<GlobalOptionSummary> searchOptionList(SearchOption searchOption, Pageable pageable);
}
