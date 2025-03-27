package me.fmroz.search.feign;

import me.fmroz.shoppi.contract.product.ProductEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/sync")
    List<ProductEventDTO> getAllActiveProducts();
}
