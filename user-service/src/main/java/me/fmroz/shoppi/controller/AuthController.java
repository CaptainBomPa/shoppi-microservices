package me.fmroz.shoppi.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.dto.LoginRequest;
import me.fmroz.shoppi.dto.LoginResponse;
import me.fmroz.shoppi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
