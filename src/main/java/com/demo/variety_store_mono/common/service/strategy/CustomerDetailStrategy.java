package com.demo.variety_store_mono.common.service.strategy;

import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.customer.request.CustomerDetailRequest;
import com.demo.variety_store_mono.customer.response.CustomerDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerDetailStrategy implements UserDetailStrategy {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public String getDetailView() {
        return "admin/content/detail/customer-detail";
    }

    @Override
    public String getEditView() {
        return "admin/content/edit/customer-edit";
    }

    @Override
    public Object getDetail(Long userId) {
        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, CustomerDetailResponse.class);
    }

    @Override
    public void updateDetail(Long userId, Object customerDetailRequest) {
        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        CustomerDetailRequest request = (CustomerDetailRequest) customerDetailRequest;

        // 사용자 기본 정보 변경.
        userService.updateUserBasicInfo(userId, request);

        // 고객 상세 정보 변경.
        user.updateCustomerDetail(request.getAddress());
    }
}
