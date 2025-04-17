package com.demo.variety_store_mono.customer.service;

import com.demo.variety_store_mono.customer.dto.request.OrderItemRequest;
import com.demo.variety_store_mono.customer.dto.request.OrderRequest;
import com.demo.variety_store_mono.customer.dto.response.OrderResponse;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.customer.entity.Order;
import com.demo.variety_store_mono.customer.entity.OrderItem;
import com.demo.variety_store_mono.customer.entity.OrderItemOption;
import com.demo.variety_store_mono.customer.repository.OrderRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public OrderResponse createOrder(OrderRequest request, Long customerId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
        Customer customer = user.getCustomer();

        // 주문 생성.
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .customer(customer)
                .build();

        for (OrderItemRequest orderItemRequest  : request.getOrderItems()) {
            Product product = productRepository.findProductCatalogDetails(orderItemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

            // 주문 상품 생성.
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(orderItemRequest.getQuantity())
                    .unitPrice(product.getBasePrice())
                    .build();

            // 옵션 처리: 선택한 옵션 값의 추가 가격 합산 및 스냅샷 기록
            for (Long optionValueId : orderItemRequest.getOptionValueIds()) {
                // 선택한 옵션 값이 실제 존재하는지 확인.
                ProductOptionValue pov = productRepository.findProductOptionValueById(optionValueId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션 값입니다."));

                OrderItemOption orderItemOption = OrderItemOption.builder()
                        .productOptionValue(pov)
                        .optionName(pov.getProductOption().getName())
                        .optionValue(pov.getOptionValue())
                        .additionalPrice(pov.getAdditionalPrice())
                        .build();

                // OrderItem에 옵션 추가 (양방향 연관관계 설정)
                orderItem.addOrderItemOption(orderItemOption);
            }
            // 단가 산출: 기본가격 + 옵션 추가 가격 합산.
            BigDecimal finalUnitPrice = orderItem.finalUnitPrice();
            // Order에 OrderItem 추가 (양방향 연관관계 설정)
            order.addOrderItem(orderItem);
        }
        // 주문 항목 총액 = 단가 * 주문 수량
        BigDecimal totalPrice = order.finalTotalPrice();

        return modelMapper.map(orderRepository.save(order), OrderResponse.class);
    }
}
