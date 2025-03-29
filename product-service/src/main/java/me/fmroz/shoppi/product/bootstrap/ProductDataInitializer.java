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
                    """
                    ### 💻 Gamingowy PC z RTX 3060
            
                    Wysokowydajny zestaw komputerowy dla graczy:
                    - Procesor: **AMD Ryzen 7 5800X**
                    - Karta graficzna: **NVIDIA GeForce RTX 3060 12GB**
                    - RAM: **32GB DDR4 3200MHz**
                    - Dysk: **SSD 1TB NVMe**
            
                    🔥 Idealny do gier AAA, streamowania i pracy kreatywnej.
                    """,
                    """
                    ### 🧑‍💼 Komputer do biura z monitorem FHD
            
                    Zestaw idealny do pracy zdalnej:
                    - Procesor: **Intel Core i5 11400**
                    - Monitor: **24" Full HD**
                    - System: **Windows 11 Pro**
            
                    ✅ Gotowy do pracy od razu po podłączeniu.
                    """,
                    """
                    ### 📦 Mini PC – kompaktowe rozwiązanie
            
                    - Rozmiar: **Ultra mini (mieści się w dłoni)**
                    - Procesor: **Intel N6005**
                    - Wyjścia: **HDMI, DisplayPort, USB-C**
            
                    Świetny do **mediów domowych**, jako **HTPC** albo mały serwer.
                    """
            )),

            Map.entry("Laptopy", List.of(
                    """
                    ### 🎮 Laptop gamingowy z RTX 4060
            
                    - Matryca: **15.6" 144Hz IPS**
                    - GPU: **RTX 4060 8GB**
                    - RAM: **16GB DDR5**
                    - Chłodzenie: **Podwójny wentylator z miedzianymi rurkami**
            
                    🔋 Do 6h na baterii | **Windows 11 Home**
                    """,
                    """
                    ### ✨ Ultrabook do pracy i podróży
            
                    - Ekran: **13.3" dotykowy OLED**
                    - Waga: **1.1 kg**
                    - Obudowa: **Aluminium klasy lotniczej**
            
                    🔋 Bateria do **14h** pracy, idealny dla studentów i profesjonalistów.
                    """,
                    """
                    ### 🧳 Laptop biznesowy z dodatkami premium
            
                    - Obudowa z włókna węglowego
                    - Klawiatura podświetlana, **czytnik linii papilarnych**
                    - System: **Windows 11 Pro**
            
                    📈 Idealny wybór dla menedżerów i pracowników korporacyjnych.
                    """
            )),

            Map.entry("Smartfony", List.of(
                    """
                    ### 📱 Flagowy smartfon z AMOLED i 5G
            
                    - Ekran: **6.8" AMOLED 120Hz**
                    - Chip: **Snapdragon 8 Gen 2**
                    - RAM: **12GB**, Pamięć: **256GB**
            
                    📸 Aparat **200MP** z trybem nocnym i nagrywaniem 8K.  
                    🔋 Szybkie ładowanie **120W** – 0% do 100% w 19 minut!
                    """,
                    """
                    ### 🍏 iPhone 14 Pro Max
            
                    - Ekran: **Super Retina XDR**, Always-On
                    - Dynamic Island, FaceID, 5G
                    - Aparat: **48MP ProRAW**, tryb filmowy
            
                    System: **iOS 17**, gwarantowane aktualizacje na lata.
                    """,
                    """
                    ### 💡 Smartfon budżetowy
            
                    - Ekran: **6.5" HD+**
                    - Procesor: **Helio G35**
                    - Dual SIM, Gniazdo Jack 3.5mm
            
                    Idealny jako **telefon zapasowy** lub dla seniora.
                    """
            )),

            Map.entry("Meble", List.of(
                    """
                    ### 🛋️ Szafa przesuwna z lustrem
            
                    - Wymiary: **200x180x60 cm**
                    - Kolor: **Dąb Sonoma**
                    - System cichy domyk, szyny aluminiowe
            
                    🪞 Z dużym lustrem, idealna do sypialni lub przedpokoju.
                    """,
                    """
                    ### 🪑 Biurko regulowane z szufladami
            
                    - Blat: **120x60 cm**, MDF lakierowany
                    - Wysokość: **regulowana elektrycznie**
                    - 2 szuflady i organizer kabli
            
                    Dla graczy, uczniów i do home office!
                    """,
                    """
                    ### 👑 Fotel ergonomiczny z podparciem lędźwiowym
            
                    - Materiał: **Siatka Mesh + Ekoskóra**
                    - Regulowane podłokietniki, zagłówek
                    - Max obciążenie: **120kg**
            
                    ✅ Dla zdrowych pleców i komfortowej pracy.
                    """
            )),

            Map.entry("Książki", List.of(
                    """
                    ### 📚 "Harry Potter i Kamień Filozoficzny"
            
                    - Autor: **J.K. Rowling**
                    - Gatunek: **Fantasy / Młodzieżowa**
                    - Stron: **320**
            
                    ✨ Pierwsza część przygód młodego czarodzieja – klasyk literatury współczesnej!
                    """,
                    """
                    ### 🧠 "Atomowe nawyki" – James Clear
            
                    - Gatunek: **Poradnik / Psychologia**
                    - Bestseller **New York Times**
            
                    Naucz się **budować dobre nawyki** i skutecznie pozbywać się złych.
                    """,
                    """
                    ### 🕵️ "Zbrodnia i kara" – Fiodor Dostojewski
            
                    - Klasyk literatury rosyjskiej
                    - Motyw winy i odkupienia
            
                    📘 Idealna lektura dla miłośników ciężkiej, głębokiej fabuły.
                    """
            )),

            Map.entry("Rowery", List.of(
                    """
                    ### 🚵 Rower Górski 27.5" z amortyzacją
            
                    - Rama: **Aluminiowa**
                    - Hamulce: **Tarcze hydrauliczne**
                    - Amortyzator: **100mm skoku**
            
                    🏞️ Gotowy na górskie szlaki i leśne trasy!
                    """,
                    """
                    ### 🏁 Rower Szosowy Carbon
            
                    - Rama: **Włókno węglowe**
                    - Przerzutki: **Shimano Tiagra**
                    - Koła: **700c z niskim oporem toczenia**
            
                    Prędkość i lekkość – dla pasjonatów wyścigów!
                    """,
                    """
                    ### 🚲 Rower miejski z koszykiem
            
                    - 3 biegi **Shimano Nexus**
                    - Bagażnik, dzwonek, błotniki
            
                    Stylowy i praktyczny. Idealny na zakupy i dojazdy do pracy.
                    """
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
