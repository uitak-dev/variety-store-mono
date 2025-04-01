package com.demo.variety_store_mono.listener;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.Seller;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final GlobalOptionRepository globalOptionRepository;
    private final ProductRepository productRepository;

    private final PasswordEncoder passwordEncoder;

    // 스레드 안전한 중복 실행 방지 플래그
    private final AtomicBoolean alreadySetup = new AtomicBoolean(false);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup.get()) {
            return;
        }
        setupData();
        alreadySetup.set(true);
    }

    private void setupData() {

        Map<String, List<User> > userMap = new HashMap<>();
        userMap.put("admin", new ArrayList<>());
        userMap.put("seller", new ArrayList<>());
        userMap.put("user", new ArrayList<>());

        // 관리자 계정 생성.
        int cnt = 0;
        for (int i = 0; i < 2; i++) {
            User admin = createUserIfNotFound("admin" + String.format("%03d", ++cnt), "qwer1234",
                    "admin" + String.format("%03d", cnt) + "@example.com", UserType.ADMIN);

            userMap.get("admin").add(admin);
        }

        // 판매자 계정 생성.
        cnt = 0;
        for (int i = 0; i < 5; i++) {
            User seller = createUserIfNotFound("seller" + String.format("%03d", ++cnt), "qwer1234",
                    "seller" + String.format("%03d", cnt) + "@example.com", UserType.SELLER);

            userMap.get("seller").add(seller);
        }

        // 일반 사용자 계정 생성.
        cnt = 0;
        for (int i = 0; i < 23; i++) {
            User user = createUserIfNotFound("user" + String.format("%03d", ++cnt), "qwer1234",
                    "user" + String.format("%03d", cnt) + "@example.com", UserType.CUSTOMER);

            userMap.get("user").add(user);
        }

        // 옵션 템플릿 생성.
        GlobalOption colorOption = createOptionIfNotFound("색상", Set.of("레드", "블루", "블랙", "화이트"));
        GlobalOption sizeOption1 = createOptionIfNotFound("사이즈(바지, 셔츠)", Set.of("S", "M", "L", "XL"));
        GlobalOption sizeOption2 = createOptionIfNotFound("사이즈(신발)", Set.of("250", "255", "260", "265", "270"));
        GlobalOption materialOption = createOptionIfNotFound("소재", Set.of("면", "실크", "폴리", "스웨이드"));

        // 카테고리 생성.
        Category clothes = createCategoryIfNotFound("의류", null, new HashSet<>());
        Category mensClothes = createCategoryIfNotFound("남성패션", clothes, new HashSet<>());
        Category womensClothes = createCategoryIfNotFound("여성패션", clothes, new HashSet<>());
        createCategoryIfNotFound("셔츠(남성)", mensClothes, Set.of(colorOption, sizeOption1, materialOption));
        createCategoryIfNotFound("바지(남성)", mensClothes, Set.of(colorOption, sizeOption1, materialOption));
        createCategoryIfNotFound("셔츠(여성)", womensClothes, Set.of(colorOption, sizeOption1, materialOption));
        createCategoryIfNotFound("바지(여성)", womensClothes, Set.of(colorOption, sizeOption1, materialOption));
        Category shoes = createCategoryIfNotFound("신발", clothes, Set.of(colorOption, sizeOption2));

        // 상품 등록.
        Seller seller001 = userMap.get("seller").get(0).getSeller();
        createProductIfNotFound(seller001, shoes, "척 70 데님 블랙", BigDecimal.valueOf(105000));
        createProductIfNotFound(seller001, shoes, "척테일러 올스타 빈티지 카고", BigDecimal.valueOf(75000));
        createProductIfNotFound(seller001, shoes, "나이키 에어 포스 1", BigDecimal.valueOf(149000));
    }

    private User createUserIfNotFound(String userName, String password, String email, UserType userType) {

        Optional<User> existingMember = userRepository.findByUserName(userName);
        if (existingMember.isPresent()) {
            return existingMember.get();
        }

        User newUser = User.builder()
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .email(email)
                .userType(userType)
                .build();

        switch (userType.name()) {
            case "ADMIN" -> newUser.createAdminDetail();
            case "SELLER" -> newUser.createSellerDetail();
            case "CUSTOMER" -> newUser.createCustomerDetail();
        }

        String description = switch (userType.name()) {
            case "ADMIN" -> "관리자";
            case "SELLER" -> "판매자";
            case "CUSTOMER" -> "일반 사용자";
            default -> "";
        };

        Role role = createRoleIfNotFound(userType.getRoleName(), description);
        newUser.addRole(role);

        return userRepository.save(newUser);
    }

    private Role createRoleIfNotFound(String roleName, String description) {

        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            return existingRole.get();
        }

        Role newRole = Role.builder()
                .name(roleName)
                .description(description)
                .build();

        return roleRepository.save(newRole);
    }

    private Category createCategoryIfNotFound(String categoryName, @Nullable Category parent,
                                              Set<GlobalOption> globalOptions) {

        Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }

        Category category = Category.builder()
                .name(categoryName)
                .parent(parent)
                .build();

        category.updateGlobalOption(globalOptions);

        return categoryRepository.save(category);
    }

    private GlobalOption createOptionIfNotFound(String optionName, Set<String> optionValues) {

        Optional<GlobalOption> existingGlobalOption = globalOptionRepository.findByName(optionName);
        if (existingGlobalOption.isPresent()) {
            return existingGlobalOption.get();
        }

        Set<GlobalOptionValue> collect = optionValues.stream()
                .map(GlobalOptionValue::new)
                .collect(Collectors.toSet());

        GlobalOption globalOption = GlobalOption.builder()
                .name(optionName)
                .globalOptionValues(collect)
                .build();

        return globalOptionRepository.save(globalOption);
    }

    private Product createProductIfNotFound(Seller seller, Category category, String productName, BigDecimal price) {

        Product product = Product.builder()
                .name(productName)
                .description("(TEST) 상품 데이터")
                .single(true)
                .basePrice(price)
                .stockQuantity(10)
                .manufactureDate(LocalDate.now())
                .seller(seller)
                .build();

        Product savedProduct = productRepository.save(product);
        category.addProduct(product);

        return savedProduct;
    }
}
