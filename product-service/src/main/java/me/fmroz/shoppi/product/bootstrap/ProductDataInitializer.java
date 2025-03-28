package me.fmroz.shoppi.product.bootstrap;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.shoppi.contract.product.Currency;
import me.fmroz.shoppi.contract.product.ProductStatus;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.model.Product;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import me.fmroz.shoppi.product.repository.ProductRepository;
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

    private static final int PRODUCT_COUNT = 1000;
    private static final Long USER_ID_START = 1L;
    private static final Long SELLER_ID_START = 31L;

    private final Random random = new Random();

    private final Map<String, List<String>> titleTemplates = Map.ofEntries(
            Map.entry("Komputery", List.of(
                    "Gamingowy PC Ryzen 7",
                    "Komputer do biura HP EliteDesk",
                    "Zestaw komputerowy z monitorem Dell",
                    "Mini PC Intel NUC",
                    "PC do obróbki grafiki - Intel i9 + RTX 4070 Ti"
            )),
            Map.entry("Laptopy", List.of(
                    "Laptop ASUS TUF Gaming F15",
                    "MacBook Pro 14'' M2",
                    "Lenovo ThinkPad X1 Carbon",
                    "Dell Inspiron 15",
                    "Ultrabook HP Spectre x360"
            )),
            Map.entry("Smartfony", List.of(
                    "iPhone 14 Pro Max 128GB",
                    "Samsung Galaxy S23 Ultra",
                    "Xiaomi Redmi Note 12",
                    "Google Pixel 7",
                    "OnePlus 11 5G"
            )),
            Map.entry("Meble", List.of(
                    "Szafa IKEA PAX 200x236",
                    "Stół drewniany rozkładany",
                    "Krzesło gamingowe Diablo X-One",
                    "Łóżko tapicerowane 160x200",
                    "Komoda MALM 6 szuflad"
            )),
            Map.entry("Książki", List.of(
                    "Harry Potter i Kamień Filozoficzny",
                    "Zbrodnia i kara - Fiodor Dostojewski",
                    "Atomic Habits - James Clear",
                    "Wiedźmin: Ostatnie Życzenie",
                    "Mały Książę - Antoine de Saint-Exupéry"
            )),
            Map.entry("Rowery", List.of(
                    "Rower górski Kross Level 3.0",
                    "Rower szosowy Giant Contend",
                    "Rower miejski Romet Pop Art",
                    "E-bike Elektryczny Ecobike",
                    "Rower dziecięcy 20 cali"
            ))
    );

    private final Map<String, List<String>> descriptionTemplates = Map.ofEntries(
            Map.entry("Komputery", List.of(
                    "Wysokowydajny PC dla graczy z kartą RTX 3060 i procesorem Ryzen 7.",
                    "Zestaw do pracy z monitorem Full HD i dyskiem SSD 1TB.",
                    "Kompaktowy mini PC idealny do zastosowań biurowych i HTPC."
            )),
            Map.entry("Laptopy", List.of(
                    "Laptop gamingowy z matrycą 144Hz i RTX 4060.",
                    "Ultrabook z ekranem dotykowym i baterią na 14h pracy.",
                    "Biznesowy notebook z obudową z włókna węglowego."
            )),
            Map.entry("Smartfony", List.of(
                    "Smartfon z aparatem 200MP, Snapdragonem 8 Gen 2 i ekranem AMOLED.",
                    "Model z szybkim ładowaniem 120W i 12GB RAM.",
                    "Nowoczesny design, FaceID, 5G oraz wytrzymała bateria."
            )),
            Map.entry("Meble", List.of(
                    "Nowoczesna szafa z przesuwanymi drzwiami i lustrem.",
                    "Biurko z regulacją wysokości oraz szufladami.",
                    "Komfortowe krzesło z podłokietnikami i poduszką lędźwiową."
            )),
            Map.entry("Książki", List.of(
                    "Bestseller fantasy – magiczny świat i epicka przygoda.",
                    "Poradnik rozwijający nawyki i produktywność.",
                    "Klasyka literatury rosyjskiej z elementami psychologicznymi."
            )),
            Map.entry("Rowery", List.of(
                    "Rower górski z amortyzacją i 21 biegami.",
                    "Szybki rower szosowy na lekkiej ramie aluminiowej.",
                    "Stylowy rower miejski z koszykiem i przerzutką Shimano."
            ))
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

        for (int i = 0; i < PRODUCT_COUNT; i++) {
            Long userId = SELLER_ID_START + random.nextInt(70);

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
