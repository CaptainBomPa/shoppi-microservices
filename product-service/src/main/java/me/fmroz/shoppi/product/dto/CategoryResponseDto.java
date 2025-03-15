package me.fmroz.shoppi.product.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;
    private List<CategoryResponseDto> subcategories;
}
