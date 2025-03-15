package me.fmroz.shoppi.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fmroz.shoppi.product.dto.AddCategoryRequest;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CategoryControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category rootCategory;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();

        rootCategory = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .parentCategory(null)
                        .build()
        );
    }

    @Test
    void shouldAddNewCategory() throws Exception {
        AddCategoryRequest request = new AddCategoryRequest();
        request.setName("Home Appliances");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
        assertThat(categories.stream().anyMatch(c -> c.getName().equals("Home Appliances"))).isTrue();
    }

    @Test
    void shouldAddSubcategory() throws Exception {
        AddCategoryRequest request = new AddCategoryRequest();
        request.setName("Laptops");
        request.setParentCategoryId(rootCategory.getId());

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        List<Category> subcategories = categoryRepository.findAll();
        assertThat(subcategories).hasSize(2);
        assertThat(subcategories.stream().anyMatch(c -> c.getName().equals("Laptops") && c.getParentCategory().equals(rootCategory))).isTrue();
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        Category phones = categoryRepository.save(
                Category.builder()
                        .name("Phones")
                        .parentCategory(rootCategory)
                        .build()
        );

        categoryRepository.save(
                Category.builder()
                        .name("Smartphones")
                        .parentCategory(phones)
                        .build()
        );

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[0].subcategories.size()").value(1))
                .andExpect(jsonPath("$[0].subcategories[0].name").value("Phones"))
                .andExpect(jsonPath("$[0].subcategories[0].subcategories.size()").value(1))
                .andExpect(jsonPath("$[0].subcategories[0].subcategories[0].name").value("Smartphones"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/" + rootCategory.getId()))
                .andExpect(status().isOk());

        assertThat(categoryRepository.findById(rootCategory.getId())).isNotPresent();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCategory() throws Exception {
        mockMvc.perform(delete("/categories/99999"))
                .andExpect(status().isNotFound());
    }
}
