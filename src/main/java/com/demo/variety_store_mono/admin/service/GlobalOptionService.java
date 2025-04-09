package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.dto.summary.GlobalOptionSummary;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.admin.dto.request.GlobalOptionRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchOption;
import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GlobalOptionService {

    private final GlobalOptionRepository optionRepository;
    private final ModelMapper modelMapper;

    /** 옵션 템플릿 생성 */
    public GlobalOptionResponse createGlobalOption(GlobalOptionRequest request) {

        Set<GlobalOptionValue> optionValues = request.getGlobalOptionValues().stream()
                .map(valueRequest -> new GlobalOptionValue(valueRequest.getOptionValue()))
                .collect(Collectors.toSet());

        GlobalOption globalOption = GlobalOption.builder()
                .name(request.getName())
                .globalOptionValues(optionValues)
                .build();

        optionRepository.save(globalOption);
        return modelMapper.map(globalOption, GlobalOptionResponse.class);
    }

    // 글로벌 옵션 전체 조회
    public List<GlobalOptionSummary> getAllOption() {
        return optionRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(option -> modelMapper.map(option, GlobalOptionSummary.class))
                .toList();
    }

    // 글로벌 옵션 목록 조회
    @Transactional(readOnly = true)
    public Page<GlobalOptionSummary> getOptionSearchList(SearchOption searchOption, Pageable pageable) {
        return optionRepository.searchOptionList(searchOption, pageable);
    }

    /** 옵션 템플릿 상세 조회 */
    @Transactional(readOnly = true)
    public GlobalOptionResponse getGlobalOption(Long globalOptionId) {
        GlobalOption globalOption = optionRepository.findOptionAndValuesById(globalOptionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션은 존재하지 않습니다."));

        return modelMapper.map(globalOption, GlobalOptionResponse.class);
    }

    /** 옵션 템플릿 수정 */
    public GlobalOptionResponse updateGlobalOption(Long globalOptionId, GlobalOptionRequest request) {
        GlobalOption globalOption = optionRepository.findOptionAndValuesById(globalOptionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션은 존재하지 않습니다."));

        Set<GlobalOptionValue> optionValues = request.getGlobalOptionValues().stream()
                .map(valueRequest -> new GlobalOptionValue(valueRequest.getOptionValue()))
                .collect(Collectors.toSet());

        globalOption.updateGlobalOption(request.getName(), optionValues);
        return modelMapper.map(globalOption, GlobalOptionResponse.class);
    }

    /** 옵션 템플릿 삭제 */
    public void removeGlobalOption(Long globalOptionId) {
        optionRepository.deleteById(globalOptionId);
    }
}
