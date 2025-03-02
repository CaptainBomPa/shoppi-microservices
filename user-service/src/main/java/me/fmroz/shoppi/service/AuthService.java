package me.fmroz.shoppi.service;

import lombok.RequiredArgsConstructor;
import me.fmroz.auth.AuthUserDetails;
import me.fmroz.auth.JwtUtil;
import me.fmroz.shoppi.dto.LoginRequest;
import me.fmroz.shoppi.dto.LoginResponse;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ShoppiUserRepository userRepository;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        ShoppiUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        AuthUserDetails userDetails = new AuthUserDetails(user.getEmail(), user.getAccountType());
        String token = JwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
