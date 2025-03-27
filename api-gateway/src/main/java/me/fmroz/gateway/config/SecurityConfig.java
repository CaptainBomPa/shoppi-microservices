package me.fmroz.gateway.config;

import lombok.extern.slf4j.Slf4j;
import me.fmroz.gateway.common.PublicEndpoints;
import me.fmroz.gateway.security.CustomAuthenticationEntryPoint;
import me.fmroz.gateway.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthFilter jwtAuthFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    List<String> originsList = Arrays.asList(allowedOrigins.split(","));
                    config.setAllowedOrigins(originsList);
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    PublicEndpoints.PUBLIC_ENDPOINTS.forEach((method, paths) -> {
                        for (String path : paths) {
                            exchange.pathMatchers(method, path).permitAll();
                        }
                    });
                    exchange.anyExchange().authenticated();
                })
                .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .headers(headers -> headers.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
