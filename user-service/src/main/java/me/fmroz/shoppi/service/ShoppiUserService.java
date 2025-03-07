package me.fmroz.shoppi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.fmroz.shoppi.dto.ChangeEmailRequest;
import me.fmroz.shoppi.dto.ChangePasswordRequest;
import me.fmroz.shoppi.dto.UpdateUserInfoRequest;
import me.fmroz.shoppi.exception.BadPasswordException;
import me.fmroz.shoppi.exception.EmailAlreadyInUseException;
import me.fmroz.shoppi.exception.MissingPasswordException;
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
    public void updatePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        ShoppiUser existingUser = findUserById(userId);

        if (!StringUtils.hasText(changePasswordRequest.getOldPassword()) ||
                !StringUtils.hasText(changePasswordRequest.getNewPassword())) {
            throw new MissingPasswordException("Passwords cannot be empty");
        }

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), existingUser.getPassword())) {
            throw new BadPasswordException("Old password is incorrect");
        }

        existingUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        shoppiUserRepository.save(existingUser);
    }

    @Transactional
    public ShoppiUser updateUserInfo(Long userId, UpdateUserInfoRequest updateUserInfoRequest) {
        ShoppiUser existingUser = findUserById(userId);

        if (StringUtils.hasText(updateUserInfoRequest.getFirstName())) {
            existingUser.setFirstName(updateUserInfoRequest.getFirstName());
        }
        if (StringUtils.hasText(updateUserInfoRequest.getLastName())) {
            existingUser.setLastName(updateUserInfoRequest.getLastName());
        }
        if (updateUserInfoRequest.getGender() != null) {
            existingUser.setGender(updateUserInfoRequest.getGender());
        }

        return shoppiUserRepository.save(existingUser);
    }

    @Transactional
    public void changeEmail(Long userId, ChangeEmailRequest changeEmailRequest) {
        ShoppiUser existingUser = findUserById(userId);

        if (!StringUtils.hasText(changeEmailRequest.getPassword()) ||
                !StringUtils.hasText(changeEmailRequest.getNewEmail())) {
            throw new MissingPasswordException("Password and new email cannot be empty");
        }

        if (!passwordEncoder.matches(changeEmailRequest.getPassword(), existingUser.getPassword())) {
            throw new BadPasswordException("Incorrect password");
        }

        if (shoppiUserRepository.findByEmail(changeEmailRequest.getNewEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }

        existingUser.setEmail(changeEmailRequest.getNewEmail());
        shoppiUserRepository.save(existingUser);
    }

    @Transactional
    public ShoppiUser findUserById(Long userId) {
        return shoppiUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    @Transactional
    public ShoppiUser getUserByEmail(String email) {
        return shoppiUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    private void validateEmailUniqueness(String email) {
        if (shoppiUserRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException("Email " + email + " is already in use");
        }
    }
}
