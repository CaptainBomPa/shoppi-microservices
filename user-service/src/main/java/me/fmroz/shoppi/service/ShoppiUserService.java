package me.fmroz.shoppi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.exception.EmailAlreadyInUseException;
import me.fmroz.shoppi.exception.UserNotFoundException;
import me.fmroz.shoppi.model.ShoppiUser;
import me.fmroz.shoppi.repository.ShoppiUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ShoppiUserService {

    private final ShoppiUserRepository shoppiUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ShoppiUser createUser(ShoppiUser user) {
        validateEmailUniqueness(user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return shoppiUserRepository.save(user);
    }

    @Transactional
    public ShoppiUser updateUser(Long userId, ShoppiUser updatedUser) {
        ShoppiUser existingUser = findUserById(userId);

        if (StringUtils.hasText(updatedUser.getFirstName())) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (StringUtils.hasText(updatedUser.getLastName())) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (StringUtils.hasText(updatedUser.getEmail())) {
            validateEmailUniqueness(updatedUser.getEmail());
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender());
        }

        return shoppiUserRepository.save(existingUser);
    }

    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        ShoppiUser existingUser = findUserById(userId);

        if (!StringUtils.hasText(newPassword)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        shoppiUserRepository.save(existingUser);
    }

    @Transactional
    public ShoppiUser findUserById(Long userId) {
        return shoppiUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    private void validateEmailUniqueness(String email) {
        if (shoppiUserRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException("Email " + email + " is already in use");
        }
    }
}
