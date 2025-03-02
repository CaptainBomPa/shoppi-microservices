package me.fmroz.shoppi.service;

import lombok.RequiredArgsConstructor;
import me.fmroz.auth.AuthUserDetails;
import me.fmroz.auth.JwtUtil;
import me.fmroz.shoppi.dto.LoginRequest;
import me.fmroz.shoppi.dto.LoginResponse;
import me.fmroz.shoppi.dto.RefreshTokenRequest;
import me.fmroz.shoppi.dto.RefreshTokenResponse;
import me.fmroz.shoppi.exception.InvalidRefreshTokenException;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ShoppiUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        ShoppiUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        AuthUserDetails userDetails = new AuthUserDetails(user.getEmail(), user.getAccountType());
        String accessToken = JwtUtil.generateAccessToken(userDetails);
        String refreshToken = JwtUtil.generateRefreshToken(userDetails);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        ShoppiUser user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid Refresh Token"));

        AuthUserDetails userDetails = new AuthUserDetails(user.getEmail(), user.getAccountType());

        if (!JwtUtil.validateToken(request.getRefreshToken(), userDetails)) {
            return ResponseEntity.status(401).build();
        }

        String newAccessToken = JwtUtil.generateAccessToken(userDetails);
        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken));
    }
}
