package me.fmroz.search.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.dto.ProductSearchRequest;
import me.fmroz.search.service.CachedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cache/products")
@RequiredArgsConstructor
public class ProductCacheController {

    private final CachedProductService cachedProductService;

    @GetMapping("/search")
    public ResponseEntity<List<CachedProduct>> searchProducts(ProductSearchRequest request) {
        if (request.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<CachedProduct> result = cachedProductService.search(request);
        return ResponseEntity.ok(result);
    }
}
