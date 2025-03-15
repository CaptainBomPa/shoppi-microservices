package me.fmroz.shoppi.product.bootstrap;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Value("${app.category.populate:false}")
    private boolean shouldPopulate;

    @Override
    @Transactional
    public void run(String... args) {
        if (!shouldPopulate) {
            log.info("Category population disabled. Skipping...");
            return;
        }

        if (categoryRepository.count() > 0) {
            log.info("Categories already exist. Skipping initialization...");
            return;
        }

        log.info("Populating default categories...");

        Category electronics = saveCategory("Elektronika", null);
        Category clothing = saveCategory("Odzież", null);
        Category home = saveCategory("Dom i Ogród", null);
        Category sports = saveCategory("Sport i Rekreacja", null);
        Category books = saveCategory("Książki i Multimedia", null);
        Category automotive = saveCategory("Motoryzacja", null);

        Category computers = saveCategory("Komputery", electronics);
        Category smartphones = saveCategory("Smartfony", electronics);
        Category laptops = saveCategory("Laptopy", electronics);
        Category agd = saveCategory("AGD", electronics);
        Category tvAudio = saveCategory("Telewizory i Audio", electronics);

        saveCategory("PC", computers);
        saveCategory("Podzespoły komputerowe", computers);
        saveCategory("Gaming", computers);
        saveCategory("Akcesoria komputerowe", computers);

        saveCategory("Android", smartphones);
        saveCategory("iPhone", smartphones);
        saveCategory("Akcesoria do smartfonów", smartphones);

        saveCategory("Ultrabooki", laptops);
        saveCategory("Gamingowe", laptops);
        saveCategory("Biznesowe", laptops);

        saveCategory("Pralki", agd);
        saveCategory("Lodówki", agd);
        saveCategory("Ekspresy do kawy", agd);
        saveCategory("Mikrofalówki", agd);

        saveCategory("Męska", clothing);
        saveCategory("Damska", clothing);
        saveCategory("Dziecięca", clothing);
        saveCategory("Obuwie", clothing);
        saveCategory("Akcesoria", clothing);

        saveCategory("Garnitury", saveCategory("Elegancka", saveCategory("Męska", clothing)));
        saveCategory("Sukienki", saveCategory("Elegancka", saveCategory("Damska", clothing)));

        saveCategory("Meble", home);
        saveCategory("Narzędzia", home);
        saveCategory("Dekoracje", home);
        saveCategory("Oświetlenie", home);
        saveCategory("Ogród", home);

        saveCategory("Sofy i Fotele", saveCategory("Meble do salonu", home));
        saveCategory("Stoły i Krzesła", saveCategory("Meble do kuchni", home));

        saveCategory("Siłownia i Fitness", sports);
        saveCategory("Rowery", sports);
        saveCategory("Sporty Zimowe", sports);
        saveCategory("Piłka Nożna", sports);

        saveCategory("Książki", books);
        saveCategory("Filmy", books);
        saveCategory("Muzyka", books);
        saveCategory("Gry", books);

        saveCategory("Fantasy", saveCategory("Fikcja", books));
        saveCategory("Biografie", saveCategory("Literatura Faktu", books));

        saveCategory("Samochody", automotive);
        saveCategory("Motocykle", automotive);
        saveCategory("Części samochodowe", automotive);
        saveCategory("Opony i Felgi", automotive);


        log.info("Default categories populated!");
    }

    private Category saveCategory(String name, Category parent) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(
                        Category.builder()
                                .name(name)
                                .parentCategory(parent)
                                .build()
                ));
    }
}
