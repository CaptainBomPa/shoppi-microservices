package me.fmroz.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, org.springframework.security.core.AuthenticationException ex) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    if (auth == null || !auth.isAuthenticated()) {
                        return new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED).commence(exchange, ex);
                    }
                    return Mono.empty();
                });
    }
}
