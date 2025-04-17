package com.demo.variety_store_mono.customer.service;

import com.demo.variety_store_mono.config.modelmapper.ModelMapperConfig;
import com.demo.variety_store_mono.customer.dto.request.OrderItemRequest;
import com.demo.variety_store_mono.customer.dto.request.OrderRequest;
import com.demo.variety_store_mono.customer.dto.response.OrderResponse;
import com.demo.variety_store_mono.customer.entity.Cart;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.customer.entity.Order;
import com.demo.variety_store_mono.customer.repository.OrderRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.Seller;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.test_data_builder.ProductOptionTestDataBuilder;
import com.demo.variety_store_mono.test_data_builder.ProductOptionValueTestDataBuilder;
import com.demo.variety_store_mono.test_data_builder.ProductTestDataBuilder;
import com.demo.variety_store_mono.test_data_builder.UserTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private OrderService orderService;

    // 더미 객체들
    private User dummyUserCustomer;
    private User dummyUserSeller;
    private Customer dummyCustomer;
    private Seller dummySeller;
    private Product dummyProduct;
    private ProductOptionValue largeSize;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        orderService = new OrderService(orderRepository, userRepository, productRepository, modelMapper);

        dummyUserCustomer = new UserTestDataBuilder(UserType.CUSTOMER).withId(1L).build();
        dummyCustomer = dummyUserCustomer.createCustomerDetail();
        ReflectionTestUtils.setField(dummyCustomer, "id", 1L);

        dummyUserSeller = new UserTestDataBuilder(UserType.SELLER).withId(11L).build();
        dummySeller = dummyUserSeller.createSellerDetail();
        ReflectionTestUtils.setField(dummySeller, "id", 11L);

        largeSize = new ProductOptionValueTestDataBuilder()
                .withId(301L)
                .withOptionValue("Large")
                .withAdditionalPrice(BigDecimal.valueOf(1000))
                .build();

        ProductOptionValue smallSize = new ProductOptionValueTestDataBuilder()
                .withId(302L)
                .withOptionValue("Small")
                .withAdditionalPrice(BigDecimal.valueOf(600))
                .build();

        ProductOption sizeOption = new ProductOptionTestDataBuilder()
                .withId(201L)
                .withName("사이즈")
                .withProductOptionValues(Set.of(largeSize, smallSize))
                .build();

        dummyProduct = new ProductTestDataBuilder()
                .withId(101L)
                .withSingle(false)
                .withBasePrice(BigDecimal.valueOf(109900))
                .withProductOption(sizeOption)
                .withSeller(dummySeller)
                .build();
    }

    @Test
    void testCreateOrder_Success() {
        // Given: 주문 요청(OrderRequest) 설정
        Long customerId = 1L;
        // OrderItemRequest: 상품 101L, 주문 수량 2, 옵션 값 301L 선택.
        OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                .productId(101L)
                .quantity(2)
                .optionValueIds(Set.of(301L))
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .orderItems(List.of(orderItemRequest))
                .build();

        // 모의 설정: userRepository.findById(customerId) -> dummyUser
        when(userRepository.findById(customerId)).thenReturn(Optional.of(dummyUserCustomer));

        // 모의 설정: productRepository.findProductCatalogDetails(productId) -> dummyProduct
        when(productRepository.findProductCatalogDetails(101L)).thenReturn(Optional.of(dummyProduct));

        // 모의 설정: productRepository.findProductOptionValueById(200L) -> dummyPov
        when(productRepository.findProductOptionValueById(301L)).thenReturn(Optional.of(largeSize));

        // 모의 설정: orderRepository.save(order) -> 주문에 ID가 부여된 order 반환 (예: ID 1000L)
        // ArgumentCaptor를 이용하여 Order 타입의 인수를 캡쳐
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        // 모의 객체의 save() 호출 결과를 캡쳐하고, 저장된 Order 객체에 ID를 부여.
        when(orderRepository.save(orderCaptor.capture())).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedOrder, "id", 1000L);
            return savedOrder;
        });

        // When: 주문 생성 서비스 호출.
        OrderResponse response = orderService.createOrder(orderRequest, customerId);

        // Then: 주문이 정상 생성되어 응답으로 매핑되는지 검증.
        assertNotNull(response, "OrderResponse는 null이 아니어야 합니다.");
        // 주문 총액 계산: 기본 가격 109900 + 옵션 추가 1000 = 110900 단가, 주문 수량 2 => 총액 221800
        BigDecimal expectedTotal = BigDecimal.valueOf(110900).multiply(BigDecimal.valueOf(2));
        // Order 엔티티의 totalPrice가 OrderResponse에 매핑되어야 하므로, 해당 값 검증
        assertEquals(expectedTotal, response.getTotalPrice(), "주문 총액이 올바르게 계산되어야 합니다.");

        // orderRepository.save()가 한 번 호출되었는지 검증.
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}