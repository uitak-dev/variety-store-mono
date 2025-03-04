package com.demo.variety_store_mono.admin.service.strategy;

import com.demo.variety_store_mono.common.entity.UserType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDetailStrategyFactory {

    private final AdminDetailStrategy adminDetailStrategy;
    private final SellerDetailStrategy sellerDetailStrategy;
    private final CustomerDetailStrategy customerDetailStrategy;

    private final Map<UserType, UserDetailStrategy> strategyMap = new EnumMap<>(UserType.class);

    // 생성자 또는 초기화 블록에서 전략 등록.
    @PostConstruct
    public void init() {
        strategyMap.put(UserType.ADMIN, adminDetailStrategy);
        strategyMap.put(UserType.SELLER, sellerDetailStrategy);
        strategyMap.put(UserType.CUSTOMER, customerDetailStrategy);
    }

    public UserDetailStrategy getStrategy(UserType userType) {
        return strategyMap.get(userType);
    }
}
