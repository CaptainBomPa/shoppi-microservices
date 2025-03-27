package me.fmroz.shoppi.product.bootstrap;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import me.fmroz.shoppi.product.repository.ProductRepository;
import me.fmroz.shoppi.contract.product.Currency;
import me.fmroz.shoppi.contract.product.ProductStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${app.data.populate:false}")
    private boolean shouldPopulate;

    private static final int PRODUCT_COUNT = 100;
    private static final Long USER_ID_START = 1L;
    private static final Long SELLER_ID_START = 31L;

    private final Random random = new Random();

    private final Map<String, List<String>> titleTemplates = Map.of(
            "Komputery", List.of("Gamingowy PC", "Zestaw Biurkowy", "Komputer Mini"),
            "Laptopy", List.of("Laptop do pracy", "Laptop gamingowy", "Ultrabook podróżny"),
            "Smartfony", List.of("Smartfon Android", "Nowy iPhone", "Tani telefon"),
            "Meble", List.of("Fotel biurowy", "Stół kuchenny", "Szafa przesuwna"),
            "Książki", List.of("Książka fantasy", "Poradnik zdrowia", "Kryminał miejski"),
            "Rowery", List.of("Rower górski", "Rower szosowy", "Rower miejski")
    );

    private final Map<String, List<String>> descriptionTemplates = Map.of(
            "Komputery", List.of("Nowoczesny zestaw komputerowy idealny do pracy i nauki.",
                    "Świetny komputer z dużą ilością RAM i SSD."),
            "Laptopy", List.of("Wydajny laptop z ekranem Full HD.",
                    "Idealny laptop do pracy zdalnej i rozrywki."),
            "Smartfony", List.of("Smartfon z dużym ekranem i wytrzymałą baterią.",
                    "Najnowszy model z aparatem 108MP."),
            "Meble", List.of("Solidne meble z drewna, nowoczesny design.",
                    "Meble idealne do salonu lub sypialni."),
            "Książki", List.of("Bestseller wśród polskich autorów.",
                    "Idealna książka na długie wieczory."),
            "Rowery", List.of("Lekka rama i amortyzacja – idealny na górskie szlaki.",
                    "Rower idealny na codzienne przejażdżki.")
    );

    @Override
    @Transactional
    public void run(String... args) {
        if (!shouldPopulate) {
            log.info("Product population disabled. Skipping...");
            return;
        }

        if (productRepository.count() > 0) {
            log.info("Products already exist. Skipping initialization...");
            return;
        }

        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.warn("No categories found. Cannot populate products.");
            return;
        }

        Map<String, List<Category>> categorized = groupCategories(categories);
        log.info("Populating sample products...");

        for (int i = 1; i <= PRODUCT_COUNT; i++) {
            boolean isUser = i <= 30;
            Long userId = isUser ? USER_ID_START + i - 1 : SELLER_ID_START + (i - 31);

            String templateKey = pickRandom(titleTemplates.keySet());
            String title = pickRandom(titleTemplates.get(templateKey));
            String description = pickRandom(descriptionTemplates.get(templateKey));
            Category category = pickRandom(categorized.getOrDefault(templateKey, categories));

            Product product = Product.builder()
                    .title(title)
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .userId(userId)
                    .price(BigDecimal.valueOf(50 + random.nextInt(1000)))
                    .currency(randomEnum(Currency.class))
                    .status(randomEnum(ProductStatus.class))
                    .quantity(1 + random.nextInt(20))
                    .promotedUntil(random.nextBoolean() ? LocalDateTime.now().plusDays(7) : null)
                    .category(category)
                    .build();

            productRepository.save(product);
        }

        log.info("Sample products populated!");
    }

    private <T> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    private String pickRandom(Set<String> keys) {
        int index = random.nextInt(keys.size());
        return new ArrayList<>(keys).get(index);
    }

    private <T> T pickRandom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private Map<String, List<Category>> groupCategories(List<Category> all) {
        return all.stream()
                .filter(cat -> cat.getParentCategory() != null)
                .collect(Collectors.groupingBy(cat -> {
                    Category parent = cat.getParentCategory();
                    while (parent.getParentCategory() != null) {
                        parent = parent.getParentCategory();
                    }
                    return parent.getName();
                }));
    }
}
