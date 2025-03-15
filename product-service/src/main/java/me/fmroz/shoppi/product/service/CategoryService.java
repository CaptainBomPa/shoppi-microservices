package me.fmroz.shoppi.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.product.dto.AddCategoryRequest;
import me.fmroz.shoppi.product.dto.CategoryResponseDto;
import me.fmroz.shoppi.product.exception.CategoryNotFoundException;
import me.fmroz.shoppi.product.model.Category;
import me.fmroz.shoppi.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addCategory(AddCategoryRequest request) {
        Category parentCategory = null;
        if (request.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found"));
        }

        Category category = Category.builder()
                .name(request.getName())
                .parentCategory(parentCategory)
                .build();

        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> rootCategories = categoryRepository.findAll().stream()
                .filter(category -> category.getParentCategory() == null)
                .toList();

        return rootCategories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryResponseDto mapToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .subcategories(category.getSubcategories().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
