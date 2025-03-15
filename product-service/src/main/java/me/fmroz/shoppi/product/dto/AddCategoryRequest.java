package me.fmroz.shoppi.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    private Long parentCategoryId;
}
