package me.fmroz.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.search.dto.CachedProduct;
import me.fmroz.search.dto.ProductSearchRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachedProductService {

    private final RedisTemplate<String, CachedProduct> redisTemplate;
    private static final String KEY_PREFIX = "product:";

    public void save(CachedProduct product) {
        String key = buildKey(product.getId());
        redisTemplate.opsForValue().set(key, product);
        log.info("Product saved to cache: {}", key);
    }

    public void delete(Long id) {
        String key = buildKey(id);
        redisTemplate.delete(key);
        log.info("Product removed from cache: {}", key);
    }

    public CachedProduct findById(Long id) {
        String key = buildKey(id);
        return redisTemplate.opsForValue().get(key);
    }

    public List<CachedProduct> search(ProductSearchRequest req) {
        return findAll().stream()
                .filter(p -> req.getText() == null ||
                        p.getTitle().toLowerCase().contains(req.getText().toLowerCase()) ||
                        p.getDescription().toLowerCase().contains(req.getText().toLowerCase()))
                .filter(p -> req.getMinPrice() == null || p.getPrice().compareTo(req.getMinPrice()) >= 0)
                .filter(p -> req.getMaxPrice() == null || p.getPrice().compareTo(req.getMaxPrice()) <= 0)
                .filter(p -> req.getCurrency() == null || p.getCurrency().equalsIgnoreCase(req.getCurrency()))
                .filter(p -> req.getCategoryId() == null || p.getCategoryId().equals(req.getCategoryId()))
                .filter(p -> req.getUserId() == null || p.getUserId().equals(req.getUserId()))
                .collect(Collectors.toList());
    }

    public List<CachedProduct> getPromotedProducts(int limit) {
        return findAll().stream()
                .filter(p -> p.getPromotedUntil() != null && LocalDateTime.now().isBefore(p.getPromotedUntil()))
                .sorted(Comparator.comparing(CachedProduct::getPromotedUntil).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<CachedProduct> getPopularCategoryProducts(int categoryLimit, int productsPerCategory) {
        Map<Long, List<CachedProduct>> byCategory = findAll().stream()
                .collect(Collectors.groupingBy(CachedProduct::getCategoryId));

        return byCategory.values().stream()
                .sorted((a, b) -> Integer.compare(b.size(), a.size()))
                .limit(categoryLimit)
                .flatMap(list -> list.stream()
                        .sorted(Comparator.comparing(CachedProduct::getId))
                        .limit(productsPerCategory))
                .collect(Collectors.toList());
    }


    public List<CachedProduct> findAll() {
        return Objects.requireNonNull(redisTemplate.keys(KEY_PREFIX + "*")).stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String buildKey(Long id) {
        return KEY_PREFIX + id;
    }
}

