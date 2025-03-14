package me.fmroz.gateway.common;

public class PublicEndpoints {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token",
            "/api/v1/users",
            "/api/v1/users/login",
            "/api/v1/users/refresh-token",
            "/api/v1/actuator/**",
            "/actuator/gateway/routes",
            "/actuator/prometheus"
    };
}
