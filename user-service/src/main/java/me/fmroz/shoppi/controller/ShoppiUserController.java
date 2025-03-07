package me.fmroz.shoppi.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.dto.ChangeEmailRequest;
import me.fmroz.shoppi.dto.ChangePasswordRequest;
import me.fmroz.shoppi.dto.UpdateUserInfoRequest;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.service.ShoppiUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ShoppiUserController {

    private final ShoppiUserService shoppiUserService;

    @GetMapping("/me")
    public ResponseEntity<ShoppiUser> me(Authentication authentication) {
        return ResponseEntity.ok(shoppiUserService.getUserByEmail(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<ShoppiUser> createUser(@RequestBody ShoppiUser user) {
        return ResponseEntity.ok(shoppiUserService.createUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppiUser> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppiUserService.findUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ShoppiUser> updateUser(@PathVariable Long userId, @RequestBody ShoppiUser updatedUser) {
        return ResponseEntity.ok(shoppiUserService.updateUser(userId, updatedUser));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        shoppiUserService.updatePassword(userId, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/update-info")
    public ResponseEntity<ShoppiUser> updateUserInfo(@PathVariable Long userId, @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return ResponseEntity.ok(shoppiUserService.updateUserInfo(userId, updateUserInfoRequest));
    }

    @PutMapping("/{userId}/change-email")
    public ResponseEntity<Void> changeEmail(@PathVariable Long userId, @RequestBody ChangeEmailRequest changeEmailRequest) {
        shoppiUserService.changeEmail(userId, changeEmailRequest);
        return ResponseEntity.ok().build();
    }
}
