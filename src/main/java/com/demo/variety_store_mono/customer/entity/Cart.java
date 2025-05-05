package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.security.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    // 장바구니에 담긴 항목들
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    // 장바구니에 담긴 항목들의 총 금액
    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Builder
    public Cart(Customer customer) {
        this.customer = customer;
        customer.assignCart(this);
    }

    // relation convenience methods
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.assignCart(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    // 장바구니에 담긴 항목의 총 금액(상품, 옵션, 수량 포함)
    public BigDecimal finalTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            // 각 장바구니 항목의 총액 = 단가 * 수량
            BigDecimal itemTotal = cartItem.finalUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }
        this.totalPrice = total;
        return total;
    }
}
