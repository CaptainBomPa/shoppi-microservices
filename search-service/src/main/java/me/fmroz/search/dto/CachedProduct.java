package me.fmroz.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedProduct implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String currency;
    private Long categoryId;
    private Long userId;
}
