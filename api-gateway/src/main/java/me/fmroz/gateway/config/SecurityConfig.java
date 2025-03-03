package me.fmroz.gateway.config;

import me.fmroz.gateway.common.PublicEndpoints;
import me.fmroz.gateway.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthFilter jwtAuthFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.PUBLIC_ENDPOINTS).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
