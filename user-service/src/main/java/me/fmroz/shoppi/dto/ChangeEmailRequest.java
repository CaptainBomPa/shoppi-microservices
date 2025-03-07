package me.fmroz.shoppi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailRequest {
    private Long userId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "New email is required")
    private String newEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
