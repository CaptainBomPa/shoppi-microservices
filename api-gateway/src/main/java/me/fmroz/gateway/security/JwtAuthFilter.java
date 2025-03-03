package me.fmroz.gateway.security;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import me.fmroz.auth.AccountType;
import me.fmroz.auth.AuthUserDetails;
import me.fmroz.auth.JwtUtil;
import me.fmroz.gateway.common.PublicEndpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            String username = JwtUtil.extractUsername(token);
            AccountType role = JwtUtil.extractUserRole(token);
            AuthUserDetails userDetails = new AuthUserDetails(username, role);

            if (!JwtUtil.validateToken(token, userDetails)) {
                log.warn("Invalid or expired token");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);

        } catch (JwtException e) {
            log.error("Token validation error: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicEndpoint(String path) {
        for (String publicEndpoint : PublicEndpoints.PUBLIC_ENDPOINTS) {
            if (path.matches(publicEndpoint.replace("**", ".*"))) {
                return true;
            }
        }
        return false;
    }
}
