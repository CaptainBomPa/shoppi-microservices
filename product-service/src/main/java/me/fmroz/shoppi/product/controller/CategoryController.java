package me.fmroz.shoppi.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.product.dto.AddCategoryRequest;
import me.fmroz.shoppi.product.dto.CategoryResponseDto;
import me.fmroz.shoppi.product.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
