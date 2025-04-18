package me.fmroz.shoppi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfo {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private ShoppiUser user;

    @Column(nullable = false)
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must match format XX-XXX")
    private String postalCode;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @Column(nullable = false)
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    private String street;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    private String country;

    @Column(nullable = false)
    @Size(min = 9, max = 15, message = "Phone number must be between 9 and 15 digits")
    private String phone;

    @Column(nullable = false)
    @Size(min = 2, max = 5, message = "Country code must be between 2 and 5 characters (e.g. +48)")
    private String countryCode;
}
