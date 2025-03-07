package me.fmroz.shoppi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import me.fmroz.shoppi.model.staticdata.Gender;

@Getter
@Setter
public class UpdateUserInfoRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private Gender gender;
}
