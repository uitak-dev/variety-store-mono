package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.request.SearchOption;
import com.demo.variety_store_mono.admin.response.GlobalOptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOptionRepository {

    /** 글로벌 옵션 목록 조회 */
    Page<GlobalOptionResponse> searchOptionList(SearchOption searchOption, Pageable pageable);
}
