package com.demo.variety_store_mono.listener;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionValueRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.Seller;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
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
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final GlobalOptionRepository globalOptionRepository;
    private final GlobalOptionValueRepository globalOptionValueRepository;
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

        /** 관리자 계정 생성. */
        int cnt = 0;
        for (int i = 0; i < 2; i++) {
            User admin = createUserIfNotFound("admin" + String.format("%03d", ++cnt), "qwer1234",
                    "admin" + String.format("%03d", cnt) + "@example.com", UserType.ADMIN);
            userMap.get("admin").add(admin);
        }

        /** 판매자 계정 생성. */
        cnt = 0;
        for (int i = 0; i < 5; i++) {
            User seller = createUserIfNotFound("seller" + String.format("%03d", ++cnt), "qwer1234",
                    "seller" + String.format("%03d", cnt) + "@example.com", UserType.SELLER);
            userMap.get("seller").add(seller);
        }

        /** 일반 사용자 계정 생성. */
        cnt = 0;
        for (int i = 0; i < 29; i++) {
            User user = createUserIfNotFound("user" + String.format("%03d", ++cnt), "qwer1234",
                    "user" + String.format("%03d", cnt) + "@example.com", UserType.CUSTOMER);
            userMap.get("user").add(user);
        }
        
        /** 옵션 템플릿 생성. */
        GlobalOptionValue khaki = createGlobalOptionValueIfNotFound("카키");
        GlobalOptionValue navi = createGlobalOptionValueIfNotFound("네이비");
        GlobalOptionValue lavender = createGlobalOptionValueIfNotFound("라벤더");
        GlobalOption colorOptionForClothes = createGlobalOptionIfNotFound("색상(상의, 하의)",
                new LinkedHashSet<>(Arrays.asList(khaki, navi, lavender)));

        GlobalOptionValue darkGray = createGlobalOptionValueIfNotFound("다크 그레이");
        GlobalOptionValue ivory = createGlobalOptionValueIfNotFound("아이보리");
        GlobalOptionValue silver = createGlobalOptionValueIfNotFound("실버");
        GlobalOptionValue black = createGlobalOptionValueIfNotFound("블랙");
        GlobalOption colorOptionForShoes = createGlobalOptionIfNotFound("색상(신발)",
                new LinkedHashSet<>(Arrays.asList(darkGray, ivory, silver, black)));

        GlobalOptionValue small = createGlobalOptionValueIfNotFound("S");
        GlobalOptionValue medium = createGlobalOptionValueIfNotFound("M");
        GlobalOptionValue large = createGlobalOptionValueIfNotFound("L");
        GlobalOptionValue extraLarge = createGlobalOptionValueIfNotFound("XL");
        GlobalOption sizeOptionForClothes = createGlobalOptionIfNotFound("사이즈(상의, 하의)",
                new LinkedHashSet<>(Arrays.asList(small, medium, large, extraLarge)));

        GlobalOptionValue milliMeter250 = createGlobalOptionValueIfNotFound("250mm");
        GlobalOptionValue milliMeter255 = createGlobalOptionValueIfNotFound("255mm");
        GlobalOptionValue milliMeter260 = createGlobalOptionValueIfNotFound("260mm");
        GlobalOptionValue milliMeter265 = createGlobalOptionValueIfNotFound("265mm");
        GlobalOptionValue milliMeter270 = createGlobalOptionValueIfNotFound("270mm");
        GlobalOptionValue milliMeter275 = createGlobalOptionValueIfNotFound("275mm");
        GlobalOption sizeOptionForShoes = createGlobalOptionIfNotFound("사이즈(신발)",
                new LinkedHashSet<>(Arrays.asList(milliMeter250, milliMeter255, milliMeter260,
                        milliMeter265, milliMeter270, milliMeter275))
        );

        GlobalOptionValue white = createGlobalOptionValueIfNotFound("화이트");
        GlobalOption colorOptionForElectronics = createGlobalOptionIfNotFound("색상(가전 제품)",
                new LinkedHashSet<>(Arrays.asList(black, white)));

        GlobalOptionValue gigaByte8 = createGlobalOptionValueIfNotFound("8GB");
        GlobalOptionValue gigaByte16 = createGlobalOptionValueIfNotFound("16GB");
        GlobalOptionValue gigaByte32 = createGlobalOptionValueIfNotFound("32GB");
        GlobalOptionValue gigaByte512 = createGlobalOptionValueIfNotFound("512GB");
        GlobalOptionValue teraByte1 = createGlobalOptionValueIfNotFound("1TB");
        GlobalOptionValue teraByte2 = createGlobalOptionValueIfNotFound("2TB");
        GlobalOption memoryCapacityOption = createGlobalOptionIfNotFound("메모리 용량",
                new LinkedHashSet<>(Arrays.asList(gigaByte8, gigaByte16, gigaByte32)));
        GlobalOption storageCapacityOption = createGlobalOptionIfNotFound("저장 용량",
                new LinkedHashSet<>(Arrays.asList(gigaByte512, teraByte1, teraByte2)));

        GlobalOptionValue hardCover = createGlobalOptionValueIfNotFound("양장");
        GlobalOptionValue audioBook = createGlobalOptionValueIfNotFound("audio-Book");
        GlobalOptionValue eBook = createGlobalOptionValueIfNotFound("e-Book");
        GlobalOption bookOption = createGlobalOptionIfNotFound("도서 형태",
                new LinkedHashSet<>(Arrays.asList(hardCover, audioBook, eBook)));

        GlobalOptionValue gram150 = createGlobalOptionValueIfNotFound("150g");
        GlobalOptionValue gram300 = createGlobalOptionValueIfNotFound("300g");
        GlobalOptionValue gram500 = createGlobalOptionValueIfNotFound("500g");
        GlobalOptionValue gram900 = createGlobalOptionValueIfNotFound("900g");
        GlobalOption weightOptionForFood = createGlobalOptionIfNotFound("개당 중량(식품)",
                new LinkedHashSet<>(Arrays.asList(gram150, gram300, gram500, gram900)));

        GlobalOptionValue pieces1 = createGlobalOptionValueIfNotFound("1개입");
        GlobalOptionValue pieces3 = createGlobalOptionValueIfNotFound("3개입");
        GlobalOptionValue pieces5 = createGlobalOptionValueIfNotFound("5개입");
        GlobalOptionValue pieces12 = createGlobalOptionValueIfNotFound("12개입");
        GlobalOptionValue pieces24 = createGlobalOptionValueIfNotFound("24개입");
        GlobalOption quantityOptionForFood = createGlobalOptionIfNotFound("수량(식품)",
                new LinkedHashSet<>(Arrays.asList(pieces1, pieces3, pieces5, pieces12, pieces24)));

        /**  카테고리 생성( 1-depth ) */
        Category clothes = createCategoryIfNotFound("의류", null, new LinkedHashSet<>());
        Category electronics = createCategoryIfNotFound("전자제품", null, new LinkedHashSet<>());
        Category books = createCategoryIfNotFound("도서", null, new LinkedHashSet<>());
        Category foods = createCategoryIfNotFound("식품", null, new LinkedHashSet<>());

        /** 카테고리 생성( 2-depth ) */
        Category shirts = createCategoryIfNotFound("상의", clothes, new LinkedHashSet<>());
        Category pants = createCategoryIfNotFound("바지", clothes, new LinkedHashSet<>());
        Category shoes = createCategoryIfNotFound("신발", clothes, 
                new LinkedHashSet<>(Arrays.asList(colorOptionForShoes, sizeOptionForShoes)));
        
        Category refrigerators = createCategoryIfNotFound("냉장고", electronics, new LinkedHashSet<>());
        Category refDry = createCategoryIfNotFound("세탁기/건조기", electronics, new LinkedHashSet<>());
        Category laptops = createCategoryIfNotFound("노트북", electronics,
                new LinkedHashSet<>(Arrays.asList(memoryCapacityOption, storageCapacityOption)));

        Category collegeTestBooks = createCategoryIfNotFound("대학 교재", books, new LinkedHashSet<>());
        Category qualifications = createCategoryIfNotFound("자격증", books, new LinkedHashSet<>());
        Category economics = createCategoryIfNotFound("경제", books, new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category healths = createCategoryIfNotFound("건강", books, new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category sciences = createCategoryIfNotFound("과학", books, new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category novels = createCategoryIfNotFound("소설", books, new LinkedHashSet<>(Arrays.asList(bookOption)));

        Category coffeeTeas = createCategoryIfNotFound("커피/차", foods,
                new LinkedHashSet<>(Arrays.asList(weightOptionForFood, quantityOptionForFood)));
        Category processedFoods = createCategoryIfNotFound("가공식품", foods,
                new LinkedHashSet<>(Arrays.asList(weightOptionForFood, quantityOptionForFood)));
        Category fruitsVegetables = createCategoryIfNotFound("과일/채소", foods,
                new LinkedHashSet<>(Arrays.asList(weightOptionForFood, quantityOptionForFood)));
        Category waterBeverages = createCategoryIfNotFound("생수/음료", foods,
                new LinkedHashSet<>(Arrays.asList(weightOptionForFood, quantityOptionForFood)));
        Category healthFunctionalFoods = createCategoryIfNotFound("건강기능식품", foods,
                new LinkedHashSet<>(Arrays.asList(weightOptionForFood, quantityOptionForFood)));

        /** 카테고리 생성( 3-depth ) */
        Category manToMans = createCategoryIfNotFound("맨투맨", shirts,
                new LinkedHashSet<>(Arrays.asList(colorOptionForClothes, sizeOptionForClothes)));
        Category shortSleeveShirts = createCategoryIfNotFound("반소매 티셔츠", shirts,
                new LinkedHashSet<>(Arrays.asList(colorOptionForClothes, sizeOptionForClothes)));
        Category etcShirts = createCategoryIfNotFound("기타 상의", shirts,
                new LinkedHashSet<>(Arrays.asList(colorOptionForClothes, sizeOptionForClothes)));
        Category denimPants = createCategoryIfNotFound("데님 팬츠", pants,
                new LinkedHashSet<>(Arrays.asList(colorOptionForClothes, sizeOptionForClothes)));
        Category etcPants = createCategoryIfNotFound("기타 하의", pants,
                new LinkedHashSet<>(Arrays.asList(colorOptionForClothes, sizeOptionForClothes)));

        Category doorRefrigerator = createCategoryIfNotFound("3/4 도어 냉장고", refrigerators,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));
        Category kimchiRefrigerator = createCategoryIfNotFound("김치 냉장고", refrigerators,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));
        Category privateRefrigerator = createCategoryIfNotFound("전용 냉장고", refrigerators,
                new LinkedHashSet<>());
        Category washingMachines= createCategoryIfNotFound("세탁기", refDry,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));
        Category dryMachines= createCategoryIfNotFound("건조기", refDry,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));
        Category clotheManagers = createCategoryIfNotFound("의류 관리기", refDry,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));

        Category humanities = createCategoryIfNotFound("인문학 계열", collegeTestBooks,
                new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category medicalFields = createCategoryIfNotFound("의/약학 계열", collegeTestBooks,
                new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category agriculturalSeries = createCategoryIfNotFound("농축산학 계열", collegeTestBooks,
                new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category estateAgents = createCategoryIfNotFound("공인중개사", qualifications,
                new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category laborAttorney = createCategoryIfNotFound("공인노무사", qualifications,
                new LinkedHashSet<>(Arrays.asList(bookOption)));
        Category socialWorker = createCategoryIfNotFound("사회복지사", qualifications,
                new LinkedHashSet<>(Arrays.asList(bookOption)));

        /** 카테고리 생성( 4-depth ) */
        Category wineCellars = createCategoryIfNotFound("와인셀러", privateRefrigerator,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));
        Category showCases = createCategoryIfNotFound("쇼케이스", privateRefrigerator,
                new LinkedHashSet<>(Arrays.asList(colorOptionForElectronics)));

        /** 상품 등록. */
        Seller seller1 = userMap.get("seller").get(0).getSeller();    // 의류
        Seller seller2 = userMap.get("seller").get(1).getSeller();    // 전자제품
        Seller seller3 = userMap.get("seller").get(2).getSeller();    // 도서
        Seller seller4 = userMap.get("seller").get(3).getSeller();    // 식품

        // 의류(상의, 하의) 상품 데이터 생성.
        Set<ProductOption> productOptions;

        productOptions = Stream.of(
                createProductOption(colorOptionForClothes, Stream.of(
                        createProductOptionValue(khaki, 100, BigDecimal.ZERO),
                        createProductOptionValue(navi, 100, BigDecimal.ZERO),
                        createProductOptionValue(ivory, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(sizeOptionForClothes, Stream.of(
                        createProductOptionValue(small, 100, BigDecimal.ZERO),
                        createProductOptionValue(medium, 100, BigDecimal.ZERO),
                        createProductOptionValue(large, 100, BigDecimal.ZERO),
                        createProductOptionValue(extraLarge, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller1, manToMans, "오버 핏 스트라이프 럭비 스웻 셔츠 (3 COLOR)",
                BigDecimal.valueOf(36800), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForClothes, Stream.of(
                        createProductOptionValue(khaki, 100, BigDecimal.ZERO),
                        createProductOptionValue(navi, 100, BigDecimal.ZERO),
                        createProductOptionValue(ivory, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(sizeOptionForClothes, Stream.of(
                        createProductOptionValue(small, 100, BigDecimal.ZERO),
                        createProductOptionValue(medium, 100, BigDecimal.ZERO),
                        createProductOptionValue(large, 100, BigDecimal.ZERO),
                        createProductOptionValue(extraLarge, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller1, manToMans, "Teddy bear 맨투맨",
                BigDecimal.valueOf(34300), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForClothes, Stream.of(
                        createProductOptionValue(khaki, 100, BigDecimal.ZERO),
                        createProductOptionValue(navi, 100, BigDecimal.ZERO),
                        createProductOptionValue(ivory, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(sizeOptionForClothes, Stream.of(
                        createProductOptionValue(small, 100, BigDecimal.ZERO),
                        createProductOptionValue(medium, 100, BigDecimal.ZERO),
                        createProductOptionValue(large, 100, BigDecimal.ZERO),
                        createProductOptionValue(extraLarge, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller1, shortSleeveShirts, "노스텔지아 피그먼트 티셔츠",
                BigDecimal.valueOf(25350), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForClothes, Stream.of(
                        createProductOptionValue(khaki, 100, BigDecimal.ZERO),
                        createProductOptionValue(navi, 100, BigDecimal.ZERO),
                        createProductOptionValue(ivory, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(sizeOptionForClothes, Stream.of(
                        createProductOptionValue(small, 100, BigDecimal.ZERO),
                        createProductOptionValue(medium, 100, BigDecimal.ZERO),
                        createProductOptionValue(large, 100, BigDecimal.ZERO),
                        createProductOptionValue(extraLarge, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller1, shortSleeveShirts, "드리밍 레글런 오버핏 반팔 티셔츠",
                BigDecimal.valueOf(24800), 100, false, productOptions);

        createProduct(seller1, etcShirts, "스포티 라인 니트 하프 집업_블랙",
                BigDecimal.valueOf(57200), 100, true, null);
        createProduct(seller1, etcShirts, "에센셜 시그니쳐 반집업 맨투맨_검정",
                BigDecimal.valueOf(29900), 100, true, null);

        createProduct(seller1, denimPants, "세미 와이드 부츠컷 데님 팬츠",
                BigDecimal.valueOf(39900), 100, true, null);
        createProduct(seller1, denimPants, "논페이드 커브드 데님 팬츠 브라운",
                BigDecimal.valueOf(57400), 100, true, null);

        createProduct(seller1, denimPants, "투 턱 나일론 팬츠 Charcoal",
                BigDecimal.valueOf(71200), 100, true, null);
        createProduct(seller1, denimPants, "H.D BDU 팬츠 - 올리브 / PU248OLIVE",
                BigDecimal.valueOf(289990), 100, true, null);

        // 전자제품(냉장고) 데이터 생성.
        productOptions = Stream.of(
                createProductOption(colorOptionForElectronics, Stream.of(
                        createProductOptionValue(black, 100, BigDecimal.ZERO),
                        createProductOptionValue(white, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller2, doorRefrigerator, "캐리어 모드비 피트인 427L 4도어 양문형 냉장고",
                BigDecimal.valueOf(729000), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForElectronics, Stream.of(
                        createProductOptionValue(black, 100, BigDecimal.ZERO),
                        createProductOptionValue(white, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller2, doorRefrigerator, "삼성전자 비스포크 875L 프리스탠딩 4도어 냉장고",
                BigDecimal.valueOf(1536600), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForElectronics, Stream.of(
                        createProductOptionValue(black, 100, BigDecimal.ZERO),
                        createProductOptionValue(white, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller2, kimchiRefrigerator, "LG전자 디오스 김치톡톡 128L 뚜껑형 1도어 김치냉장고",
                BigDecimal.valueOf(478000), 100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(colorOptionForElectronics, Stream.of(
                        createProductOptionValue(black, 100, BigDecimal.ZERO),
                        createProductOptionValue(white, 100, BigDecimal.ZERO)
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller2, kimchiRefrigerator, "LG전자 오브제 컬렉션 402L 스탠드형 4도어 김치냉장고 메탈 디오스",
                BigDecimal.valueOf(1810000), 100, false, productOptions);

        createProduct(seller2, wineCellars, "하이얼 아쿠아 와인셀러 HWC85MDB",
                BigDecimal.valueOf(326000), 100, true, null);
        createProduct(seller2, showCases, "하이얼 다용도 쇼케이스 냉장고",
                BigDecimal.valueOf(448000), 100, true, null);

        // 전자제품(노트북) 데이터 생성.
        productOptions = Stream.of(
                createProductOption(memoryCapacityOption, Stream.of(
                        createProductOptionValue(gigaByte8, 100, BigDecimal.valueOf(80000)),
                        createProductOptionValue(gigaByte16, 100, BigDecimal.valueOf(160000)),
                        createProductOptionValue(gigaByte32, 100, BigDecimal.valueOf(320000))
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(storageCapacityOption, Stream.of(
                        createProductOptionValue(gigaByte512, 100, BigDecimal.valueOf(50000)),
                        createProductOptionValue(teraByte1, 100, BigDecimal.valueOf(100000)),
                        createProductOptionValue(teraByte2, 100, BigDecimal.valueOf(200000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller2, laptops, "LG전자 2024 울트라 PC 15 코어i5 인텔 13세대",
                BigDecimal.valueOf(749000), 100, false, productOptions);
        createProduct(seller2, laptops, "레노버 2024 LOQ 15ARP9 라이젠7 라이젠 7000 시리즈 지포스 RTX 4060",
                BigDecimal.valueOf(1059000), 100, false, productOptions);

        // 도서 데이터 생성.
        createProduct(seller3, economics, "행동하지 않으면 인생은 바뀌지 않는다", BigDecimal.valueOf(15210),
                100, true, null);
        createProduct(seller3, economics, "모르면 호구 되는 경제상식", BigDecimal.valueOf(18000),
                100, true, null);
        createProduct(seller3, healths, "느리게 나이 드는 습관", BigDecimal.valueOf(16200),
                100, true, null);
        createProduct(seller3, healths, "면역력 내 몸을 살린다", BigDecimal.valueOf(2700),
                100, true, null);
        createProduct(seller3, sciences, "이기적 유전자(40주년 기념판)", BigDecimal.valueOf(18000),
                100, true, null);
        createProduct(seller3, sciences, "Cosmos", BigDecimal.valueOf(13500),
                100, true, null);

        productOptions = Stream.of(
                createProductOption(bookOption, Stream.of(
                        createProductOptionValue(hardCover, 100, BigDecimal.valueOf(2000)),
                        createProductOptionValue(audioBook, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(eBook, 100, BigDecimal.valueOf(3000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller3, novels, "어둔 밤을 지키는 야간약국", BigDecimal.valueOf(15300),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(bookOption, Stream.of(
                        createProductOptionValue(hardCover, 100, BigDecimal.valueOf(2000)),
                        createProductOptionValue(audioBook, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(eBook, 100, BigDecimal.valueOf(3000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller3, novels, "우리에게 남은 시간 46일", BigDecimal.valueOf(15120),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(bookOption, Stream.of(
                        createProductOptionValue(hardCover, 100, BigDecimal.valueOf(2000)),
                        createProductOptionValue(audioBook, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(eBook, 100, BigDecimal.valueOf(3000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller3, novels, "봉제인형 살인사건", BigDecimal.valueOf(13500),
                100, false, productOptions);

        // 식품 데이터 생성.
        productOptions = Stream.of(
                createProductOption(weightOptionForFood, Stream.of(
                        createProductOptionValue(gram150, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(gram300, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(gram500, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(gram900, 100, BigDecimal.valueOf(9000))
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, coffeeTeas, "맥심 모카골드 마일드 커피믹스", BigDecimal.valueOf(22450),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(weightOptionForFood, Stream.of(
                        createProductOptionValue(gram150, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(gram300, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(gram500, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(gram900, 100, BigDecimal.valueOf(9000))
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, coffeeTeas, "프렌치카페 카페믹스 마일드", BigDecimal.valueOf(13010),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(weightOptionForFood, Stream.of(
                        createProductOptionValue(gram150, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(gram300, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(gram500, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(gram900, 100, BigDecimal.valueOf(9000))
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, processedFoods, "오뚜기 칼칼한 돼지고기김치찜", BigDecimal.valueOf(7500),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(weightOptionForFood, Stream.of(
                        createProductOptionValue(gram150, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(gram300, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(gram500, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(gram900, 100, BigDecimal.valueOf(9000))
                ).collect(Collectors.toCollection(LinkedHashSet::new))),
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, processedFoods, "비비고 소고기듬뿍설렁탕", BigDecimal.valueOf(7480),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, fruitsVegetables, "곰곰 스테비아 대추방울토마토", BigDecimal.valueOf(5720),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, fruitsVegetables, "델몬트 필리핀 바나나", BigDecimal.valueOf(5660),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, waterBeverages, "제주삼다수", BigDecimal.valueOf(12960),
                100, false, productOptions);

        productOptions = Stream.of(
                createProductOption(quantityOptionForFood, Stream.of(
                        createProductOptionValue(pieces1, 100, BigDecimal.valueOf(0)),
                        createProductOptionValue(pieces3, 100, BigDecimal.valueOf(3000)),
                        createProductOptionValue(pieces5, 100, BigDecimal.valueOf(5000)),
                        createProductOptionValue(pieces12, 100, BigDecimal.valueOf(12000)),
                        createProductOptionValue(pieces24, 100, BigDecimal.valueOf(24000))
                ).collect(Collectors.toCollection(LinkedHashSet::new)))
        ).collect(Collectors.toCollection(LinkedHashSet::new));
        createProduct(seller4, waterBeverages, "나랑드사이다 제로", BigDecimal.valueOf(10600),
                100, false, productOptions);
    }

    /** 사용자 생성. */
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

    /** 역할/권한 생성. */
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

    /** 카테고리 생성. */
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

    /** 상품 글로벌 옵션 생성. (옵션 템플릿) */
    private GlobalOption createGlobalOptionIfNotFound(String optionName, Set<GlobalOptionValue> optionValues) {

        Optional<GlobalOption> existingGlobalOption = globalOptionRepository.findByName(optionName);
        if (existingGlobalOption.isPresent()) {
            return existingGlobalOption.get();
        }

        GlobalOption globalOption = GlobalOption.builder()
                .name(optionName)
                .globalOptionValues(optionValues)
                .build();

        return globalOptionRepository.save(globalOption);
    }

    /** 상품 글로벌 옵션 값 생성. */
    private GlobalOptionValue createGlobalOptionValueIfNotFound(String optionValue) {

        Optional<GlobalOptionValue> existingOptionValue = globalOptionValueRepository.findByOptionValue(optionValue);
        if (existingOptionValue.isPresent()) {
            return existingOptionValue.get();
        }

        GlobalOptionValue newOptionValue = new GlobalOptionValue(optionValue);
        return globalOptionValueRepository.save(newOptionValue);
    }

    /** 상품 데이터 생성. */
    private Product createProduct(Seller seller, Category category,
                                  String productName, BigDecimal price, int stockQuantity, boolean single,
                                  Set<ProductOption> productOptionList) {

        Product product = Product.builder()
                .primaryCategory(category)
                .name(productName)
                .description("(TEST) 상품 데이터 설명")
                .single(single)
                .basePrice(price)
                .stockQuantity(stockQuantity)
                .manufactureDate(LocalDate.now())
                .seller(seller)
                .build();

        // 옵션 상품인 경우, 연관관계 처리.
        if ( !(single || productOptionList.isEmpty()) ) {
            productOptionList.forEach(product::addProductOption);
        }

        Product savedProduct = productRepository.save(product);
        category.addProduct(product);

        product.approve();
        return savedProduct;
    }

    /** 글로벌 옵션을 사용해서 상품 옵션 생성. */
    private ProductOption createProductOption(GlobalOption globalOption, Set<ProductOptionValue> productOptionValues) {

        ProductOption productOption = ProductOption.builder()
                .global(true)
                .globalOption(globalOption)
                .name(globalOption.getName())
                .build();

        productOptionValues.forEach(productOption::addProductOptionValue);
        return productOption;
    }

    /** 글로벌 옵션 값을 사용해서 상품 옵션 값 생성. */
    private ProductOptionValue createProductOptionValue(GlobalOptionValue globalOptionValue,
                                                        int stockQuantity, BigDecimal additionalPrice) {

        return ProductOptionValue.builder()
                .global(true)
                .optionValue(globalOptionValue.getOptionValue())
                .additionalPrice(additionalPrice)
                .stockQuantity(stockQuantity)
                .build();
    }
}
