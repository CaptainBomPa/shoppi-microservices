package me.fmroz.search.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSearchRequest {
    private String text;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String currency;
    private Long categoryId;
    private Long userId;

    public boolean isEmpty() {
        return text == null &&
                minPrice == null &&
                maxPrice == null &&
                currency == null &&
                categoryId == null &&
                userId == null;
    }
}
