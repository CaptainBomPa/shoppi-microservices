package me.fmroz.shoppi.controller;

import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.service.ShoppiUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ShoppiUserController {

    private final ShoppiUserService shoppiUserService;

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
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @RequestBody String newPassword) {
        shoppiUserService.updatePassword(userId, newPassword);
        return ResponseEntity.noContent().build();
    }
}
