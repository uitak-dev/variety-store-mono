package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.seller.entity.Product;
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
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    // 상품의 주문 가격( 옵션 포함 )
    @Column(nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<CartItemOption> cartItemOptions = new LinkedHashSet<>();

    @Builder
    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    void assignCart(Cart cart) {
        this.cart = cart;
    }

    public void addCartItemOption(CartItemOption cartItemOption) {
        cartItemOptions.add(cartItemOption);
        cartItemOption.assignCartItem(this);
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal finalUnitPrice() {
        BigDecimal ret = product.getBasePrice();
        for (CartItemOption option : cartItemOptions) {
            ret = ret.add(option.getProductOptionValue().getAdditionalPrice());
        }
        unitPrice = ret;
        return ret;
    }
}