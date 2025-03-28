package me.fmroz.gateway.common;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicEndpoints {

    public static final Map<HttpMethod, List<String>> PUBLIC_ENDPOINTS = new HashMap<>();

    static {
        PUBLIC_ENDPOINTS.put(HttpMethod.GET, List.of(
                //user-service
                "/api/v1/users",

                // product-service
                "/api/v1/categories",
                "/api/v1/products",
                "/api/v1/products/batch",

                // search-service
                "/api/v1/cache/products/search",
                "/api/v1/cache/products/home/promoted",
                "/api/v1/cache/products/home/popular",

                // gateway internal
                "/actuator/gateway/routes",
                "/actuator/prometheus"
        ));

        PUBLIC_ENDPOINTS.put(HttpMethod.POST, List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/refresh-token",
                "/api/v1/users"
        ));
    }

    public static boolean isPublic(HttpMethod method, String path) {
        return PUBLIC_ENDPOINTS.getOrDefault(method, List.of()).stream()
                .anyMatch(pattern -> path.matches(pattern.replace("**", ".*")));
    }
}
